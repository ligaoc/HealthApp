@echo off
chcp 65001 >nul
title 健康管理App Mock服务

echo ========================================
echo    健康管理App Mock后端服务
echo ========================================
echo.

cd /d "%~dp0"

:: 检查 Node.js 是否安装
where node >nul 2>nul
if %errorlevel% neq 0 (
    echo [错误] 未检测到 Node.js，请先安装 Node.js
    echo 下载地址: https://nodejs.org/
    pause
    exit /b 1
)

:: 显示 Node.js 版本
echo [信息] Node.js 版本:
node -v
echo.

:: 检查并清理端口占用
echo [信息] 检查端口 3000 占用情况...
for /f "tokens=5" %%a in ('netstat -ano ^| findstr ":3000 " ^| findstr "LISTENING"') do (
    echo [信息] 发现端口 3000 被进程 %%a 占用，正在终止...
    taskkill /F /PID %%a >nul 2>nul
    if %errorlevel% equ 0 (
        echo [信息] 已终止进程 %%a
    )
)
echo [信息] 端口 3000 已就绪
echo.

:: 检查是否已安装依赖
if not exist "node_modules" (
    echo [信息] 首次运行，正在安装依赖...
    npm install
    if %errorlevel% neq 0 (
        echo [错误] 依赖安装失败
        pause
        exit /b 1
    )
    echo.
)

echo [信息] 正在启动 Mock 服务...
echo [信息] 按 Ctrl+C 可停止服务
echo ========================================
echo.

npm start

pause
