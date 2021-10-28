package cn.handsome.core.test;

import cn.handsome.core.gcode.CodeHelper;
import cn.handsome.core.gcode.CodeType;
import cn.handsome.core.utils.EncryptionUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * @author shoy
 * @date 2021/9/8
 */
@Slf4j
public class EncryptionUtilTest {
    @Test
    public void encryptTest() {
        final String publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAovio66G/lp86WnQ2wOUq6qutwsH14IxcULO4AiM1JGJjT15iYgDc4h03vgvEDhsNtLfxg9diLM2ZfptLjoU6PL9ottBZAZsvCEvHaxT1jW8a2DZKM0LtpPdDAhd5DMb0ez0nylael+rQFZVr5rdmzywt9957/kjG4aFKFcqGUwL5j7rGs1c4/PcrMm+d5oaHr5TliTe6JnZsOLdAzHjEyAiG+K9824YiJraFtjywfn8z77Fv+NkrAniAqpnKleAxLvax4ECLHJbbbkL3DWOjAKY6CaSmomSSepIrU41Cm0bhVBarRouYwAEh1AuAj4kVRNnHq4ocpzDas8P2PTiBQwIDAQAB";
        final String privateKey = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCi+Kjrob+WnzpadDbA5Srqq63CwfXgjFxQs7gCIzUkYmNPXmJiANziHTe+C8QOGw20t/GD12IszZl+m0uOhTo8v2i20FkBmy8IS8drFPWNbxrYNkozQu2k90MCF3kMxvR7PSfKVp6X6tAVlWvmt2bPLC333nv+SMbhoUoVyoZTAvmPusazVzj89ysyb53mhoevlOWJN7omdmw4t0DMeMTICIb4r3zbhiImtoW2PLB+fzPvsW/42SsCeICqmcqV4DEu9rHgQIsclttuQvcNY6MApjoJpKaiZJJ6kitTjUKbRuFUFqtGi5jAASHUC4CPiRVE2cerihynMNqzw/Y9OIFDAgMBAAECggEACejW1KUcMjtyX+erIKWOq4BO64XBvqqqPln/OVoMtC6rXTse/liRFcqzBgJPJJBuj+uTavHgzVqfF7y3pm3hcaot4tfeYaOfA4EgGhnHKJd16Wm7r0xFiP9j+q+Us7sxSIEad0o3EIO9rWJNklTakoRcMzM7FOY1+ahGCfcrncZ6om52n4zc6XqmsAhcqh8l4+Zw7PA2pn5KKwXaLidiyWeLcl+II5Gm7/oFf0CJvSJ86b1KI2sGL3NpVRYs8qoPadk/i8HF5QJBfc6e/cVV18o/IlkvGRweLSv4lCwEM7FO4D7fuKXoJvKpHm3AKcLkKfqKvnc1+TxuW/zdAqdx0QKBgQDaq4jZ6QjUzZt/76fHflkdD6MNodyDJ7wMz3f2Gs5yJW8E0eNryew6XFigrEfnhacWPRkHaqytR0ZRxbM8azJv0mjCqUikq6+aoEDAnBt/omLXMFHwT2PGF8U/FYO3mJXDOEm/Oqx7l1pnqpaBVVxePo5Q+2eDQsdLayqOv4abqwKBgQC+yvEqp0OBQ589XW/5tVfAIFdKfDGMhL0HQKVLFD0d+JjPshKicH+ZLvh9+pmxKOG/w0jzRS+JMyjh1ll655xXTkAdBKWxtphVSySHvw55gdjQuhTiPwKkmiqqlDXYYBC8XNQOD34v7S0daIzK1oiVkD0n3SvB5kgNTB5BK5DYyQKBgQCsaILGDQ6BensYLATFLcFlQGb24J/UaQfDsfCLgiczqIAX9MSyv8oZWaYnSK6IxMAwsIuDZYAwPvoajx36jSp/MA6ZLI+yBgu2hpOHSY4E3CvCLr7sEixAi41hZ70qjIvIGCFNPR2xdGw7QCzaXEsvo3LiNtYukABCXoR3eFrRcQKBgBqPH6FDZXdCFZGyRxsork11cxFQVolsFPBEhH3+FD/u0j7fWA66wFhvnHHVB74MpdhO9AHBWMutjnxZnV0pjq7smqovcm1ZsLz219/5racmdysfLDcOdGy7dUMHnLNBNDM0bOSELZm4V3ZnjmnSEXNYf9rjw+qbBt6rAO8rhF9ZAoGAVKPjcsnY+uiDvwf6MfIOW498fdvxGTBJ3lfT+Cm7uLs+mE9y3yDFVFdlJr8tRgBYyJ1NSOtnUwbrtKJU6v7m581pUgsJ9+/Fua/nQsp1Rvn8JhT1FRXIaRgRDFwbXyux5+5H9eR2uSiZITeA2Ic/6QMgJxYWtmStGBd5z4qpEyE=";
        String code = CodeHelper.random(CodeType.NumberAndUpperLetter, 10);

        String encrypt = EncryptionUtil.rsaEncrypt(code, publicKey);
        String decrypt = EncryptionUtil.rsaDecrypt(encrypt, privateKey);
        log.info("code:{}", code);
        log.info("encrypt:{}", encrypt);
        log.info("decrypt:{}", decrypt);
    }
}
