# Environment Attribute Getter

**English** | [中文](README-zh.md)

A mod allows you to get the value of environment attribute in the world.

## Content

This mod provides two ways to get emviromment attribute:

- Add a command `/environment_attribute`
  - Get the current server value `/environment_attribute query <environment attribute id>`
  - Export all of current server value to file `/environment_attribute exportall`
- Add a new debug option category, including all of the debug options of environment attribute
  - Only available for some of environment attributes. These attributes will be synced to the client-side, and not use complex data type.