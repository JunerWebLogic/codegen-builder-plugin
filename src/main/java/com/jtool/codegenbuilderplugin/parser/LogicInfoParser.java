package com.jtool.codegenbuilderplugin.parser;

import com.jtool.codegenbuilderplugin.model.LogicInfo;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogicInfoParser {

    public static List<LogicInfo> parseLogicInfoFromFiles(List<File> files) {

        List<LogicInfo> result = new ArrayList<>();

        for(File file : files) {
            try {

                List<LogicInfo> logicInfoList = new ArrayList<>();

                //读出文件内容
                String content = FileUtils.readFileToString(file);

                //把<logicInfo></logicInfo>之间的内容用正则表达式提取出来
                Pattern pattern = Pattern.compile("<logicInfo>(.+?)</logicInfo>", Pattern.DOTALL);
                Matcher matcher = pattern.matcher(content);

                while(matcher.find()) {
                    LogicInfo logicInfo = new LogicInfo();
                    logicInfo.setInfo(matcher.group(1));
                    logicInfo.setFileName(file.getName());

                    logicInfoList.add(logicInfo);
                }

                //把出现的第几行纪录下来
                int listIndex = 0;
                int lineIndex = 1;

                LineIterator it = FileUtils.lineIterator(file, "UTF-8");
                try {
                    while (it.hasNext()) {
                        String line = it.nextLine();
                        if (line.contains("<logicInfo>")) {
                            LogicInfo logicInfo = logicInfoList.get(listIndex);
                            logicInfo.setLineNum(lineIndex);
                            listIndex++;
                        }
                        lineIndex++;
                    }
                } finally {
                    it.close();
                }

                result.addAll(logicInfoList);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return result;
    }
}
