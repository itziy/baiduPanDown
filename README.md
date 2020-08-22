# baiduPanDown
自动化下载百度网盘的资源文件

# 这是一个完全合规合法的下载方式，只是协助自动化下载，没有任何提速的方式，大家自己买百度官网的vip套餐，反正也不贵

# 使用步骤说明

# step 1 先决条件
## 1.1 安装好Java的运行环境，推荐jdk8
## 1.2 下载好官网的百度网盘的下载工具，并且登录好你的svip
## 1.3 下载安装好Chrome浏览器

# step 2 运行前准备
## 2.1 请在你的操作系统环境下，配置好下面的环境变量，一共2个需要配置
###    webdriver.chrome.driver=/Users/rain/Downloads/chromedriver  # 驱动位置，请根据自己的环境配置
###    pan.save.path=/Users/rain/Downloads/panDown                 # 下载文件的最终存储路径，请根据自己的环境配置


# step 3 Chrome配置启动
## 3.1 /Applications/Google\ Chrome.app/Contents/MacOS/Google\ Chrome --remote-debugging-port=9222 --user-data-dir="/tmp"  # 端口代码写死了，mac下启动例子

# step 4 启动程序
## java -jar GatherBaiduPan.jar 网盘地址 提取码
## 提取码是可选参数，若存在提取码，则必须传入该参数
## 使用举例 java -jar GatherBaiduPan.jar https://pan.baidu.com/s/15RCD_zAeYWTjh2pe6mD8kg 2333
