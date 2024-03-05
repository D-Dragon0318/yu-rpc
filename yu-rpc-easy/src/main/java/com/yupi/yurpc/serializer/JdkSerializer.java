package com.yupi.yurpc.serializer;

import java.io.*;

/**
 * JDK 序列化器
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @learn <a href="https://codefather.cn">编程宝典</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
public class JdkSerializer implements Serializer {

    /**
     * 序列化
     *
     * @param object
     * @param <T>
     * @return
     * @throws IOException
     */
    @Override
    public <T> byte[] serialize(T object) throws IOException {
        // 使用ByteArrayOutputStream来存放序列化后的字节流
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        // 创建ObjectOutputStream对象，用于序列化对象
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        // 序列化对象，并将序列化后的字节流写入到outputStream中
        objectOutputStream.writeObject(object);
        // 关闭ObjectOutputStream对象，释放资源
        objectOutputStream.close();
        // 将ByteArrayOutputStream中的字节流转换成字节数组并返回
        return outputStream.toByteArray();
    }

    /**
     * 反序列化
     *
     * @param bytes
     * @param type
     * @param <T>
     * @return
     * @throws IOException
     */
    @Override
    public <T> T deserialize(byte[] bytes, Class<T> type) throws IOException {
        // 检查输入的字节数组是否为空或零长度的字节数组
        if (bytes == null || bytes.length == 0) {
            // 处理空或零长度的字节数组输入。
            throw new IllegalArgumentException("输入字节数组不能为空");
        }

        // 使用字节数组创建一个输入流，并用缓冲流包装以提高性能。
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new BufferedInputStream(inputStream))) {
            try {
                // 从对象输入流中读取一个对象，并将其强制转换为指定类型T。
                return type.cast(objectInputStream.readObject());
            } catch (ClassNotFoundException e) {
                // 如果在读取对象过程中找不到类，则抛出运行时异常。
                throw new RuntimeException("反序列化时找不到类定义", e);
            }
        } catch (IOException e) {
            // 处理IO异常。
            throw new IOException("反序列化过程中发生I/O错误", e);
        }
    }
}
