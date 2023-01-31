package com.zerobase.everycampingbackend.config;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * DB 비밀번호 등 비밀키를 암호화 하기 위한 config 입니다.
 * 아래 사이트에서 암호화, 복호화 가능합니다.
 * https://www.devglan.com/online-tools/jasypt-online-encryption-decryption
 *
 * 복호화 하기위해서는 JASYPT_SECRET_KEY가 필요하며, 환경변수로 입력해 줘야 합니다.
 * 따라서 JASYPT_SECRET_KEY는 별도 채널로 관리하여야 합니다.
 */
@Configuration
public class JasyptConfig {

    @Bean("jasyptStringEncryptor")
    public StringEncryptor stringEncryptor() {

        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();
        config.setPassword(System.getenv("JASYPT_SECRET_KEY"));
        config.setAlgorithm("PBEWithMD5AndDES");
        config.setKeyObtentionIterations("1000");
        config.setPoolSize("1");
        config.setProviderName("SunJCE");
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
        config.setIvGeneratorClassName("org.jasypt.iv.NoIvGenerator");
        config.setStringOutputType("base64");
        encryptor.setConfig(config);
        return encryptor;
    }
}
