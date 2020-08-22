package com.rain.baidu.pan;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;
import java.util.concurrent.TimeUnit;


public class GatherBaiduPan {
    private static long sleepTime = 5000L;

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("* 使用方式 java -jar GatherBaiduPan.jar 网盘地址 提取码");
            System.out.println("* 提取码是可选参数，若存在提取码，则必须传入该参数");
            System.out.println("* 使用举例 java -jar GatherBaiduPan.jar https://pan.baidu.com/s/15RCD_zAeYWTjh2pe6mD8kg 2333");
            System.exit(0);
        }

        String panUrl = args[0];
        String accessCode = args.length > 1 ? args[1] : "";

        if (!panUrl.toLowerCase().contains("https://pan.baidu.com/s/")) {
            System.err.println("百度网盘下载地址: " + panUrl + " 不合法，必须包含 https://pan.baidu.com/s/");
            System.exit(-1);
        }

        String chromeDriver = System.getenv("webdriver.chrome.driver");

        if (null == chromeDriver || chromeDriver.isEmpty()) {
            System.err.println("请先通过系统环境变量配置好Chrome driver的驱动文件完整路径 例如 名称: webdriver.chrome.driver 值: /Users/rain/Downloads/chromedriver");
            System.exit(-2);
        }

        String savePath = System.getenv("pan.save.path");

        if (null == savePath || savePath.isEmpty()) {
            System.err.println("请先通过系统环境变量配置好文件下载最终的存储完整路径 例如 名称: pan.save.path 值: /Users/rain/Downloads/panDown");
            System.exit(-3);
        }



        System.setProperty("webdriver.chrome.driver", chromeDriver);
        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("debuggerAddress", "127.0.0.1:9222");

        WebDriver driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        try {
            driver.get(panUrl);
            Thread.sleep(sleepTime);
            // 如果存在需要输入提取码
            if (driver.getTitle().contains("提取码")) {
                if (accessCode.isEmpty()) {
                    System.err.println("下载: " + panUrl + " 是需要提取码的，但是入参未提供，无法自动下载");
                    System.exit(-4);
                }
                if (((ChromeDriver) driver).findElementById("accessCode") == null) {
                    System.err.println("百度网盘页面改版了，提取码到输入框找不到了基于id:accessCode");
                    System.exit(-5);
                }
                WebElement accessCodeInput = ((ChromeDriver) driver).findElementById("accessCode");
                accessCodeInput.sendKeys(accessCode);

                WebElement accessCodeSendButton =  ((ChromeDriver) driver).findElementsByTagName("a").stream().filter(r -> r.getAttribute("title").contains("提取文件")).findFirst().orElse(null);

                if (null == accessCodeSendButton) {
                    System.err.println("百度网盘页面改版了，提交提取码按钮不到了基于a标签的title:提取文件");
                    System.exit(-6);
                }

                accessCodeSendButton.click();

                Thread.sleep(sleepTime);
            }

            if (((ChromeDriver) driver).findElementsByClassName("icon-download") == null) {
                System.err.println("百度页面改版了，无法获取到下载按钮了。。。。");
                System.exit(-7);
            }

            WebElement downloadButton = ((ChromeDriver) driver).findElementsByClassName("icon-download").stream().findFirst().orElseGet(null);

            downloadButton.click();

            Thread.sleep(sleepTime * 4);
            File saveDir = new File(savePath);
            if (!saveDir.exists() || !saveDir.isDirectory() || !saveDir.canRead()) {
                System.err.println("存储的目录: " + savePath + " 不存在或不可读");
                System.exit(-8);
            }

            File[] fileList = saveDir.listFiles();
            if (null == fileList) {
                System.err.println("存储的目录: " + savePath + " 读不到任何的文件");
                System.exit(-9);
            }
            boolean findDown = false;
            for (int i = 0; i < fileList.length; i++) {
                if (fileList[i].getName().startsWith(".")) {
                    continue;
                }
                findDown = true;
                break;
            }

            if (!findDown) {
                System.err.println("很遗憾，存储的目录: " + savePath + " 并未发现有自动开始下载的迹象");
                System.exit(-10);
            }

            System.out.println("恭喜你，执行完成，具体的下载动作由百度网盘官网的下载工具来自动完成");

            System.exit(0);

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getMessage());
        }
    }
}
