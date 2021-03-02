package com.cn.image;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cn.image.common.AESUtils;
import com.cn.image.common.Constants;
import com.cn.image.common.Utils;
import com.ulisesbocchio.jasyptspringboot.EncryptablePropertyResolver;

/**
 * @Desc 
 * @author magiczrl@foxmail.com
 * @date 2019年10月14日 下午7:35:57
 */
@Configuration
public class EncryptionPropertyConfig {

    private static Logger logger = LoggerFactory.getLogger(EncryptionPropertyConfig.class);

    /**
     * 
     * @return
     */
    @Bean(name = "encryptablePropertyResolver")
    public EncryptablePropertyResolver encryptablePropertyResolver() {
        logger.info("encryptablePropertyResolver ...");
        return new EncryptionPropertyResolver();
    }

    /**
     * 
     * @author Z.R.L
     *
     */
    class EncryptionPropertyResolver implements EncryptablePropertyResolver {

        @Override
        public String resolvePropertyValue(String value) {
            logger.debug("plain value {}", value);
            if (null != value && value.startsWith("{mycipher}")) {
                logger.info("resolvePropertyValue {}", value);
                String decValue = AESUtils.decrypt(Utils.byteConvert(Constants.CONFIG_FACTOR),
                        value.substring("{mycipher}".length()));
                logger.debug("resolvePropertyValue dec {}", decValue);
                return decValue;
            }
            return value;
        }

    }
}
