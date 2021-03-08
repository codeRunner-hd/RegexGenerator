package com.xwintop.xJavaFxTool.controller.codeTools;

import com.xwintop.xJavaFxTool.view.codeTools.RegexGeneratorView;
import com.xwintop.xcore.util.javafx.JavaFxViewUtil;
import javafx.fxml.FXML;
import javafx.scene.control.cell.MapValueFactory;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.configuration.PropertiesConfiguration;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ClassName: RegexTesterController
 * @Description: 正则表达式生成工具
 * @author: admin
 * @date: 2018/1/21 0021 1:02
 */
@Getter
@Setter
@Slf4j
public class RegexGeneratorController extends RegexGeneratorView {

    final static int DOUBLE_CLICK = 2;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            initView();
            initEvent();
        } catch (Exception e) {
            log.error("initialize error", e);
        }
    }

    private void initView() throws Exception {
        examplesTableColumn0.setCellValueFactory(new MapValueFactory("column0"));
        examplesTableColumn1.setCellValueFactory(new MapValueFactory("column1"));
        PropertiesConfiguration propertiesConfiguration = new PropertiesConfiguration("data/regexData.properties");
        propertiesConfiguration.getKeys().forEachRemaining((String key) -> {
            Map<String, String> map = new HashMap<>(16);
            map.put("column0", key);
            map.put("column1", propertiesConfiguration.getString(key));
            examplesTableView.getItems().add(map);
        });
        matchTableColumn0.setCellValueFactory(new MapValueFactory("column0"));
        matchTableColumn1.setCellValueFactory(new MapValueFactory("column1"));
        matchTableColumn2.setCellValueFactory(new MapValueFactory("column2"));
        matchTableColumn3.setCellValueFactory(new MapValueFactory("column3"));
        matchTableColumn4.setCellValueFactory(new MapValueFactory("column4"));
    }

    private void initEvent() {
        examplesTableView.setOnMouseClicked(event -> {
            if (event.getClickCount() == DOUBLE_CLICK) {
                regexTextField.setText(examplesTableView.getSelectionModel().getSelectedItem().get("column1"));
            }
        });
        regexTextField.textProperty().addListener((observable, oldValue, newValue) -> regulateAction());
        sourceTextArea.textProperty().addListener((observable, oldValue, newValue) -> regulateAction());
    }

    @FXML
    private void regulateAction() {
        String regexText = regexTextField.getText().trim();
        String sourceText = sourceTextArea.getText().trim();
        String replaceText = replaceTextField.getText();
        matchTableView.getItems().clear();
        matchTextArea.setText(null);
        Pattern p;
        if (ignoreCaseCheckBox.isSelected()) {
            // 不区分大小写
            p = Pattern.compile(regexText, Pattern.CASE_INSENSITIVE);
        } else {
            p = Pattern.compile(regexText);
        }
        // 用Pattern类的matcher()方法生成一个Matcher对象
        Matcher m = p.matcher(sourceText);
        StringBuilder sb = new StringBuilder();
        // 替换匹配
        StringBuffer rsb = new StringBuffer();
        // 使用find()方法查找第一个匹配的对象
        boolean result = m.find();
        // 使用循环找出模式匹配的内容替换之,再将内容加到sb里
        // 匹配总数
        int cnt = 0;
        int start;
        int end;
        while (result) {
            // 替换匹配
            m.appendReplacement(rsb, replaceText);
            cnt++;
            sb.append("\n");
            start = m.start();
            end = m.end();
            String matchText = sourceText.substring(start, end);
            sb.append("Match[").append(cnt).append("]: ");
            sb.append(matchText);
            sb.append(" [start: ").append(start).append(", end: ").append(end).append("]");
            String str0 = m.group();
            String str1 = "";
            try {
                //捕获的子序列
                str1 = m.group(1);
            } catch (Exception ignored) {
            }
            Map<String, String> map = new HashMap<>(16);
            map.put("column0", Integer.toString(cnt));
            map.put("column1", str0);
            map.put("column2", str1);
            map.put("column3", Integer.toString(start));
            map.put("column4", Integer.toString(end));
            matchTableView.getItems().add(map);
            result = m.find();
        }
        sb.insert(0, "\n匹配总数: " + cnt);
        sb.insert(0, "\t直接匹配判断: " + sourceText.matches(regexText));
        if (isReplaceCheckBox.isSelected() && replaceText.length() != 0) {
            m.appendTail(rsb);
            sb.append("\n\n替换匹配后内容: \n").append(rsb);
        }
        matchTextArea.setText(sb.length() > 0 ? sb.substring(1) : "");
    }

    @FXML
    private void resetAction() {
        regexTextField.setText(null);
        sourceTextArea.setText(null);
        matchTextArea.setText(null);
        matchTableView.getItems().clear();
    }

    @FXML
    private void aboutRegularAction() {
        String url = "/com/xwintop/xJavaFxTool/web/littleTools/正则表达式教程.html";
        JavaFxViewUtil.openUrlOnWebView(url, "正则表达式教程", null);
    }
}
