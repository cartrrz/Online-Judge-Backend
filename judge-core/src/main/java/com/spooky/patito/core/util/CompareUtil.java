package com.spooky.patito.core.util;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Component
public class CompareUtil {


    private static final Logger LOGGER = LogManager.getLogger(CompareUtil.class);

    public boolean isOutputTheSame(String standardOutputFilePath,
                                   String outputFilePath) throws IOException {
        File stdFile = new File(standardOutputFilePath);
        File file = new File(outputFilePath);

        LineIterator stdFileItr = FileUtils.lineIterator(stdFile, "UTF-8");
        LineIterator fileItr = FileUtils.lineIterator(file, "UTF-8");
        boolean isFileOutputTheSame = isFileOutputTheSame(stdFileItr, fileItr);

        LineIterator.closeQuietly(stdFileItr);
        LineIterator.closeQuietly(fileItr);
        return isFileOutputTheSame;
    }


    private boolean isFileOutputTheSame(LineIterator stdFileItr, LineIterator fileItr) {
        try {
            while ( stdFileItr.hasNext() && fileItr.hasNext() ) {
                String stdLine = stdFileItr.nextLine();
                String line = fileItr.nextLine();

                if ( !isLineOutputTheSame(stdLine, line) ) {
                    return false;
                }
            }
            while ( stdFileItr.hasNext() ) {
                String line = stdFileItr.nextLine();
                if ( !isLineEmpty(line, 0) ) {
                    return false;
                }
            }
            while ( fileItr.hasNext() ) {
                String line = fileItr.nextLine();
                if ( !isLineEmpty(line, 0) ) {
                    return false;
                }
            }
        } catch ( OutOfMemoryError ex ) {
            LOGGER.catching(ex);
            return false;
        }
        return true;
    }


    private boolean isLineOutputTheSame(String stdLine, String line) {
        int i = 0, j = 0;
        for ( ; i < stdLine.length() && j < line.length(); ++ i, ++ j ) {
            if (  stdLine.charAt(i) != line.charAt(j) ) {
                if ( stdLine.charAt(i) == '\n' ) {
                    if ( !isLineEmpty(line, j) ) {
                        return false;
                    }
                    return true;
                } else if ( line.charAt(j) == '\n' ) {
                    if ( !isLineEmpty(stdLine, i) ) {
                        return false;
                    }
                    return true;
                }
                return false;
            }
        }
        while ( i < stdLine.length() ) {
            if ( !isLineEmpty(stdLine, i) ) {
                return false;
            }
            ++ i;
        }
        while ( j < line.length() ) {
            if ( !isLineEmpty(line, j) ) {
                return false;
            }
            ++ j;
        }
        return true;
    }


    private boolean isLineEmpty(String line, int startIndex) {
        for ( int i = startIndex; i < line.length(); ++ i ) {
            if ( !(line.charAt(i) == ' ' || line.charAt(i) == '\n') ) {
                return false;
            }
        }
        return true;
    }

}