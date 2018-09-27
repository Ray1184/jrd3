package org.jrd3.engine.core.utils;

import org.jrd3.engine.core.exceptions.JRD3Exception;

import java.awt.image.*;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

import static org.lwjgl.BufferUtils.createByteBuffer;

public class CommonUtils {

    public static String loadResource(String fileName) throws JRD3Exception {
        String result;
        try (InputStream in = CommonUtils.class.getClass().getResourceAsStream(fileName);
             Scanner scanner = new Scanner(in, "UTF-8")) {
            result = scanner.useDelimiter("\\A").next();
        } catch (IOException e) {
            throw new JRD3Exception(e);
        }
        return result;
    }

    public static List<String> readAllLines(String fileName) throws JRD3Exception {
        List<String> list = new ArrayList<>();
        InputStream is = CommonUtils.class.getClass().getResourceAsStream(fileName);
        if (is == null) {
            try {
                is = new FileInputStream(fileName);
            } catch (IOException e) {
                throw new JRD3Exception(e);
            }
        }
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            String line;
            while ((line = br.readLine()) != null) {
                list.add(line);
            }
        } catch (IOException e) {
            throw new JRD3Exception(e);
        }
        return list;
    }

    public static int[] listIntToArray(List<Integer> list) {
        int[] result = list.stream().mapToInt((Integer v) -> v).toArray();
        return result;
    }

    public static float[] listToArray(List<Float> list) {
        int size = list != null ? list.size() : 0;
        float[] floatArr = new float[size];
        for (int i = 0; i < size; i++) {
            floatArr[i] = list.get(i);
        }
        return floatArr;
    }

    public static boolean existsResourceFile(String fileName) {
        boolean result;
        try (InputStream is = CommonUtils.class.getResourceAsStream(fileName)) {
            result = is != null;
        } catch (IOException excp) {
            result = false;
        }
        return result;
    }

    public static ByteBuffer ioResourceToByteBuffer(String resource, int bufferSize) throws JRD3Exception {
        ByteBuffer buffer;

        Path path = Paths.get(resource);
        if (Files.isReadable(path)) {
            try (SeekableByteChannel fc = Files.newByteChannel(path)) {
                buffer = createByteBuffer((int) fc.size() + 1);
                while (fc.read(buffer) != -1) ;
            } catch (IOException e) {
                throw new JRD3Exception(e);
            }
        } else {
            try (
                    InputStream source = CommonUtils.class.getResourceAsStream(resource);
                    ReadableByteChannel rbc = Channels.newChannel(source)) {
                buffer = createByteBuffer(bufferSize);

                while (true) {
                    int bytes = rbc.read(buffer);
                    if (bytes == -1) {
                        break;
                    }
                    if (buffer.remaining() == 0) {
                        buffer = resizeBuffer(buffer, buffer.capacity() * 2);
                    }
                }
            } catch (IOException e) {
                throw new JRD3Exception(e);
            }
        }

        buffer.flip();
        return buffer;
    }

    public static Properties loadProperties() throws JRD3Exception {
        Properties prop = new Properties();
        InputStream input = null;

        try {
            String propFileName = Thread.currentThread().getContextClassLoader()
                    .getResource("Config.properties").getFile();
            input = new FileInputStream(propFileName.replaceAll("%20", " "));
            prop.load(input);

        } catch (IOException ex) {
            throw new JRD3Exception(ex);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    throw new JRD3Exception(e);
                }
            }
        }

        return prop;
    }

    public static ByteBuffer inputStreamToByteBuffer(InputStream source, int bufferSize) throws JRD3Exception {
        ByteBuffer buffer;

        try (
                ReadableByteChannel rbc = Channels.newChannel(source)) {
            buffer = createByteBuffer(bufferSize);

            while (true) {
                int bytes = rbc.read(buffer);
                if (bytes == -1) {
                    break;
                }
                if (buffer.remaining() == 0) {
                    buffer = resizeBuffer(buffer, buffer.capacity() * 2);
                }
            }
        } catch (IOException e) {
            throw new JRD3Exception(e);
        }

        buffer.flip();
        return buffer;
    }

    public static void flipImage(BufferedImage image) {
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight() / 2; j++) {
                int tmp = image.getRGB(i, j);
                image.setRGB(i, j, image.getRGB(i, image.getHeight() - j - 1));
                image.setRGB(i, image.getHeight() - j - 1, tmp);
            }
        }
    }

    public static ByteBuffer convertImageData(BufferedImage bi) throws JRD3Exception {
        ByteBuffer byteBuffer;
        DataBuffer dataBuffer = bi.getRaster().getDataBuffer();

        if (dataBuffer instanceof DataBufferByte) {
            byte[] pixelData = ((DataBufferByte) dataBuffer).getData();
            byteBuffer = ByteBuffer.wrap(pixelData);
        } else if (dataBuffer instanceof DataBufferUShort) {
            short[] pixelData = ((DataBufferUShort) dataBuffer).getData();
            byteBuffer = ByteBuffer.allocate(pixelData.length * 2);
            byteBuffer.asShortBuffer().put(ShortBuffer.wrap(pixelData));
        } else if (dataBuffer instanceof DataBufferShort) {
            short[] pixelData = ((DataBufferShort) dataBuffer).getData();
            byteBuffer = ByteBuffer.allocate(pixelData.length * 2);
            byteBuffer.asShortBuffer().put(ShortBuffer.wrap(pixelData));
        } else if (dataBuffer instanceof DataBufferInt) {
            int[] pixelData = ((DataBufferInt) dataBuffer).getData();
            byteBuffer = ByteBuffer.allocate(pixelData.length * 4);
            byteBuffer.asIntBuffer().put(IntBuffer.wrap(pixelData));
        } else {
            throw new JRD3Exception("Not implemented for data buffer type: " + dataBuffer.getClass());
        }
        byteBuffer.flip();
        return byteBuffer;

    }

    private static ByteBuffer resizeBuffer(ByteBuffer buffer, int newCapacity) {
        ByteBuffer newBuffer = createByteBuffer(newCapacity);
        buffer.flip();
        newBuffer.put(buffer);
        return newBuffer;
    }

}
