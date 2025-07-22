package cn.handsome.workflow;

import cn.handsome.workflow.domain.ExternalStorageLocation;
import cn.handsome.workflow.enums.StorageOperation;
import cn.handsome.workflow.enums.StoragePayloadType;

import java.io.InputStream;

/**
 *
 * @author luoyong
 * @date 2025/7/22
 */
public interface IExternalPayloadStorage {

    /**
     * upload
     * @param path path
     * @param payload payload
     * @param payloadSize payloadSize
     */
    void upload(String path, InputStream payload, long payloadSize);

    /**
     * download
     * @param path path
     * @return InputStream
     */
    InputStream download(String path);

    /**
     * Delete
     * @param paths paths
     * @return count
     */
    int deleteObjects(String... paths);

    /**
     * getLocation
     * @param operation operation
     * @param payloadType payloadType
     * @param path path
     * @return location
     */
    ExternalStorageLocation getLocation(StorageOperation operation, StoragePayloadType payloadType, String path);

    /**
     * getLocation
     * @param operation operation
     * @param payloadType payloadType
     * @param path path
     * @param payloadBytes payloadBytes
     * @return location
     */
    default ExternalStorageLocation getLocation(
            StorageOperation operation, StoragePayloadType payloadType, String path, byte[] payloadBytes) {
        return getLocation(operation, payloadType, path);
    }
}
