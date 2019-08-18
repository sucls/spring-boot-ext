package com.sucl.fileupload.util;

import com.sucl.fileupload.model.FilePart;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author sucl
 * @since 2019/8/16
 */
public class FileUploadUtils {

    public void uploadFileRandomAccessFile(FilePart part,String path) throws IOException {
        String tempDirPath = path + part.getMd5();
        String chunkFileName = part.getFileName() + ".tmp";
        File chunkFile = new File(tempDirPath, chunkFileName);
        FileUtils.forceMkdirParent(chunkFile);

        RandomAccessFile accessTmpFile = new RandomAccessFile(chunkFile, "rw");
        long offset = part.getPartSize() * part.getIndex();

        accessTmpFile.seek(offset);
        accessTmpFile.write(part.getFile().getBytes());
        accessTmpFile.close();

    }

    public void uploadFileByMappedByteBuffer(FilePart part,String path) throws IOException {
        String fileName = part.getFileName();
        String uploadDirPath = path + part.getMd5();
        String chunkFileName = fileName + ".tmp";
        File chunkFile = new File(uploadDirPath, chunkFileName);
        FileUtils.forceMkdirParent(chunkFile);

        RandomAccessFile tempRaf = new RandomAccessFile(chunkFile, "rw");
        long offset = part.getPartSize() * part.getIndex();

        FileChannel fileChannel = tempRaf.getChannel();
        byte[] fileData = part.getFile().getBytes();
        MappedByteBuffer mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, offset, fileData.length);
        mappedByteBuffer.put(fileData);
        mappedByteBuffer.clear();
        fileChannel.close();
    }
}
