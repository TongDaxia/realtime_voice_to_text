package com.tyg.speech.util;

import io.netty.buffer.ByteBuf;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class AudioUtils {
    public static byte[] convertByteBufToArray(ByteBuf buf) {
        byte[] bytes = new byte[buf.readableBytes()];
        buf.readBytes(bytes);
        return bytes;
    }

    public static byte[] readAudioFile(String filePath) throws IOException {
        return Files.readAllBytes(Paths.get(filePath));
    }
}