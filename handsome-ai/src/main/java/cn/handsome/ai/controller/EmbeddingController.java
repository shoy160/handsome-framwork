package cn.handsome.ai.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.InputStreamResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 *
 * @author luoyong
 * @date 2025/9/2
 */
@RestController
@RequestMapping("/ai/embedding")
@RequiredArgsConstructor
public class EmbeddingController {
    private final EmbeddingModel embeddingModel;
    private final VectorStore vectorStore;

    @GetMapping()
    public void embed(@RequestParam(value = "message") String message) {
        System.out.println(Arrays.toString(embeddingModel.embed(message)));
        System.out.println("ok");
    }

    @PostMapping("vector")
    public List<String> vectorStore(@RequestParam(name = "file") MultipartFile file) throws Exception {
        // 从IO流中读取文件
        TikaDocumentReader tikaDocumentReader =
                new TikaDocumentReader(new InputStreamResource(file.getInputStream()));
        // 将文本内容划分成更小的块
        List<Document> splitDocuments = new TokenTextSplitter()
                .apply(tikaDocumentReader.read());
        // 存入向量数据库，这个过程会自动调用embeddingModel,将文本变成向量再存入。
        vectorStore.add(splitDocuments);

        return splitDocuments.stream().map(Document::getText).collect(toList());
    }

    @GetMapping("vector")
    public List<String> vectorSearch(@RequestParam(name = "text") String text) {
        SearchRequest request = SearchRequest.builder().query(text).topK(5).build();
        List<Document> documents = vectorStore.similaritySearch(request);
        return documents.stream().map(Document::getText).collect(toList());
    }
}
