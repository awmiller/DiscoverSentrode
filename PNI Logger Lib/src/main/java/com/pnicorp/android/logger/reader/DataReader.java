package com.pnicorp.android.logger.reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class DataReader {

    protected BufferedReader mBufferedReader;
    private File mDir;
    private String mFilename;
    private boolean mIsOpen = false;
    

    public DataReader(File dir) {
        mDir = dir;
    }

    public void open(String filename) throws IOException {
        
        if (mIsOpen) {
            close();
        }
        
        File file = new File(mDir, filename);

        // setup com.pnicorp.android.logger.reader
        if (mBufferedReader == null) {
            try {
                setupReader(file);
            } catch (IOException e) {
                throw new IOException("Error opening com.pnicorp.android.logger.reader", e);
            }
        }
        mFilename = filename;
        mIsOpen = true;
    }

    public String readln() throws IOException {
        if (mBufferedReader == null) {
            throw new IOException("DataReader not open for " + mFilename);
        }

        // read data
        try {
            return mBufferedReader.readLine();
        } catch (IOException e) {
            throw new IOException("Error reading from " + mFilename, e);
        }
    }

    public void close() throws IOException {
        if ((mBufferedReader != null) && (mIsOpen)) {
            try {
                mBufferedReader.close();
                mBufferedReader = null;
            } catch (IOException e) {
                throw new IOException("Error closing " + mFilename, e);
            }
        }
        mIsOpen = false;
    }

    protected void setupReader(File file) throws IOException {
        try {
            FileReader fw = new FileReader(file);
            mBufferedReader = new BufferedReader(fw);
        } catch (IOException e) {
            throw new IOException("Error creating com.pnicorp.android.logger.reader: " + file, e);
        }
    }

}
