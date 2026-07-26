# 环境属性查看器

[English](README.md) | **中文**

允许你在世界中查看环境属性的值.

## 内容

这个Mod提供了两个查看环境属性的值的方法：

- 增加了一条命令`/environment_attribute`
  - 获取当前服务端的值`/environment_attribute query <environment attribute id>`
  - 将服务端的值导出到文件中`/environment_attribute exportall`
- 增加了一个调试选项分类，包含所有与环境属性相关的调试选项。
  - 仅适用于部分环境属性，包括会同步到客户端的环境属性，以及没有使用复杂数据类型的环境属性。