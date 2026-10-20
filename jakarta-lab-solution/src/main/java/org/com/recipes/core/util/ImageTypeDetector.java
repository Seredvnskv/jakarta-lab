package org.com.recipes.core.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Arrays;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ImageTypeDetector {
    private static final byte[] PNG_SIGNATURE = {(byte) 0x89, 'P', 'N', 'G', '\r', '\n', 0x1A, '\n'};
    private static final byte[] JPEG_SIGNATURE = {(byte) 0xFF, (byte) 0xD8, (byte) 0xFF};

    public static final String IMAGE_PNG = "image/png";
    public static final String IMAGE_JPEG = "image/jpeg";

    public static String detectImageType(byte[] image) {
        if (matchesSignature(image, PNG_SIGNATURE)) {
            return IMAGE_PNG;
        }
        if (matchesSignature(image, JPEG_SIGNATURE)) {
            return IMAGE_JPEG;
        }
        throw new IllegalArgumentException("Invalid image data");
    }

    private static boolean matchesSignature(byte[] data, byte[] signature) {
        return data.length >= signature.length && Arrays.equals(data, 0, signature.length, signature, 0, signature.length);
    }
}
