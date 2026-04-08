# ApexOS - Enterprise AI Agent Operating System

<div align="center">

**AI Agent Operating System for Enterprise Applications**

[![License](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)
[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.4-brightgreen.svg)](https://spring.io/projects/spring-boot)

</div>

---

## 📋 Overview

ApexOS is an AI Agent operating system designed for enterprise applications. It features a modular design with built-in core drivers and supports dynamic installation and extension of applications through the Gitee discovery mechanism.

### Key Features

- 🚀 **Minimalist Design**: Contains only core drivers and essential system services
- 🔌 **Built-in Drivers**: All SPI drivers and basic drivers are built-in
- 📦 **Dynamic Extension**: Easily install and extend applications through Gitee
- 🔐 **Secure and Reliable**: MIT open source license, commercially usable
- 📚 **Comprehensive Documentation**: Complete installation, usage, and development documentation

---

## 🚀 Quick Start

### Requirements

- **Java**: JDK 21 or higher
- **Maven**: 3.6+
- **Memory**: At least 512MB available memory
- **Disk**: At least 500MB available space

### Installation

#### Option 1: Download Pre-built Version

```bash
# Download latest version from Gitee
wget https://gitee.com/ooderCN/apexos/releases/download/v3.0.2/apex-os-3.0.2.jar

# Start Application
java -jar apex-os-3.0.2.jar
```

#### Option 2: Build from Source

```bash
# Clone Project
git clone https://gitee.com/ooderCN/apexos.git
cd apexos

# Compile Project
mvn clean compile -DskipTests

# Package Project
mvn clean package -DskipTests

# Start Application
java -jar target/apex-os-3.0.2.jar
```

### Access System

Open browser and visit: http://localhost:8086

- Default username: `admin`
- Default password: `admin123`

---

## 📄 License

This project is open sourced under the [MIT License](LICENSE).

---

## 📞 Contact

- **Project Homepage**: https://gitee.com/ooderCN/apexos
- **Issue Tracker**: https://gitee.com/ooderCN/apexos/issues
- **Email**: onecode@ooder.cn

---

<div align="center">

**If this project helps you, please give it a ⭐️ Star!**

Made with ❤️ by ooder Team

</div>
