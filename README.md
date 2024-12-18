# Pomodoro Timer Application

This project implements a Pomodoro Timer application built with **Jetpack Compose**, **ViewModel**, **StateFlow**, and **Room** database, leveraging **MPAndroidChart** for data visualization. It aims to help users manage their productivity by providing a structured work-rest cycle, task management features, and insights into their progress over time.

## Key Features

1. **Pomodoro Cycle Management**:  
   - **TimerModel** defines default durations for work sessions, short breaks, and long breaks (in seconds).
   - **TimerController** manages the countdown logic using `viewModelScope` coroutines. It exposes real-time states (`_timeLeft`, `_isRunning`, `_isWorking`, `_cycleInfo`) through `StateFlow` objects.
   - After each cycle completes, the controller automatically switches between work and rest states. Whether the user is in auto or manual mode determines whether the next cycle starts automatically or a prompt is displayed.

2. **Automatic & Manual Modes**:  
   - Users can toggle between automatic and manual modes for cycle transitions.
   - **Auto Mode**: After completing a full round (including long breaks), a prompt notifies the user before starting the next cycle.
   - **Manual Mode**: A prompt is shown at the end of every cycle, giving the user control over when to proceed.

3. **Task Management**:  
   - **TaskController** and **TaskModel** interact with the database to allow CRUD operations on tasks.
   - Users can create, select, and edit tasks, associating each work session with a specific task.
   - Time spent on tasks is recorded and accumulated. This makes it easier to track productivity across different tasks.

4. **Data Persistence and Access (Room Database)**:  
   - **AppDatabase**, **TaskDao**, and **CycleDao** handle data storage.
   - **TaskDao** supports inserting, updating, deleting, and querying tasks, as well as retrieving time spent (TaskTimeStat).
   - **CycleDao** records completed Pomodoro cycles and provides aggregate data on a daily and trending basis.
   - A **TaskContentProvider** is also included, enabling external access to task data if needed.

5. **Progress Tracking and Visualization**:  
   - The **ProgressScreen** uses Flow-based APIs (`getCyclesPerDayFlow()`, `getTaskTimeStatsFlow()`, `getCyclesTrendFlow()`) to dynamically update charts with real-time data.
   - **DailyCycleBarChart**: Displays how many Pomodoro cycles are completed each day.
   - **TaskTimePieChart**: Illustrates the distribution of total time spent on each task, using MPAndroidChart to render a pie chart.
   - **CycleTrendLineChart**: Visualizes the trend of cycle completions over time, helping users identify consistency or improvement.
   - Additional charts or statistical breakdowns (e.g., work time distribution) can be integrated similarly.

6. **User Interface (Jetpack Compose)**:  
   - **TimerScreen**: Shows the current countdown timer, selected task, current cycle type (work/short break/long break), and controls for start, pause, and reset.
   - **TaskScreen**: Allows viewing and managing tasks, as well as selecting a task for the timer.
   - **TaskDialog** and **EditTaskDialog**: Provide dialogs for selecting and editing tasks on-the-fly.
   - **SettingsScreen**: Allows adjusting work, short break, and long break durations, as well as toggling between automatic and manual modes. Users can save settings and optionally refresh the timer.
   - **ProgressScreen**: Offers visual insights into daily cycle completions, task time distribution, and cycle completion trends.

   The UI is reactive: StateFlow updates automatically reflect in the UI without manual refreshes. Material3 components and layouts ensure a modern and polished appearance.

7. **Lifecycle and Service Integration**:  
   - **MainActivity** showcases binding to a **TimerService** (though not heavily utilized in the given code, it’s prepared to handle background timing tasks if needed).
   - Activities like **TaskActivity** and **ProgressActivity** initialize their own controllers and set their UI content, following a modular approach.


## Running the Application

1. Open the project in Android Studio.
2. Ensure that Kotlin, Jetpack Compose, and Gradle versions are up to date.
3. Run the app on an Android device or emulator.
4. On launch, choose a task (or create one), then start the Pomodoro timer. 
   Adjust settings or view progress charts through the bottom navigation.

## Future Improvements

- Implement notifications or a foreground service for background timing.
- Add more comprehensive analytics and filtering options in the ProgressScreen.
- Introduce user authentication and synchronization with remote storage if desired.
---
# 番茄工作法定时器应用

本项目是一个基于 **Jetpack Compose**、**ViewModel**、**StateFlow**、**Room** 数据库，并结合 **MPAndroidChart** 进行数据可视化的番茄工作法定时器应用。通过有序的工作与休息周期、灵活的任务管理和直观的数据分析，本应用旨在帮助用户高效安排时间、追踪工作进度与习惯趋势。
## 核心特性

1. **番茄周期管理**：  
   - **TimerModel** 定义了工作时间、短休息和长休息的默认时长（单位：秒）。  
   - **TimerController** 利用 `viewModelScope` 启动协程，实现计时逻辑，并通过 StateFlow 对外暴露实时状态（如 `_timeLeft`、`_isRunning`、`_isWorking`、`_cycleInfo`）。  
   - 当计时完成后，控制器自动在工作/休息周期之间切换。自动或手动模式的选择决定了下一阶段是自动开始还是弹窗提示用户。

2. **自动/手动模式**：  
   - 用户可在设置中切换自动/手动模式。  
   - **自动模式**：在完成一轮（包括长休息）后弹出提示，以帮助用户轻松进入下一个工作周期。  
   - **手动模式**：每个周期结束时都会有提示，用户可自主决定何时开始下一个周期。

3. **任务管理与进度记录**：  
   - **TaskController** 搭配 **TaskModel** 与数据库交互，实现任务的增删改查。  
   - 用户可创建、选择、编辑任务，并在计时开始前指定当前任务。  
   - 应用会记录任务的累计用时与完成的番茄周期数，帮助用户了解各任务的耗时与完成度。

4. **数据持久化与访问 (Room 数据库)**：  
   - **AppDatabase**、**TaskDao**、**CycleDao** 负责数据存储和检索。  
   - **TaskDao** 支持对任务数据的增删改查，同时提供任务时间统计 (TaskTimeStat)。  
   - **CycleDao** 记录完成的番茄周期，并可根据日期查询周期完成数量和趋势。  
   - **TaskContentProvider** 可提供外部访问数据的接口（如有需要）。

5. **进度跟踪与数据可视化**：  
   - **ProgressScreen** 利用 Flow 数据 (`getCyclesPerDayFlow()`、`getTaskTimeStatsFlow()`、`getCyclesTrendFlow()`) 实时更新图表。  
   - **DailyCycleBarChart**：展示每日完成番茄周期数的柱状图。  
   - **TaskTimePieChart**：使用 MPAndroidChart 绘制饼图，直观显示各任务累计用时分布。  
   - **CycleTrendLineChart**：折线图展示周期完成趋势，帮助用户了解最近的工作节奏和进步情况。

   后续可根据需求添加更多可视化图表（如工作时间分布图）。

6. **用户界面 (Jetpack Compose)**：  
   - **TimerScreen**：显示当前倒计时、任务、周期状态以及开始/暂停/重置按钮。  
   - **TaskScreen**：任务列表显示与管理，可从中选择或新建任务。  
   - **TaskDialog**、**EditTaskDialog**：通过对话框方式快速选择和编辑任务。  
   - **SettingsScreen**：允许用户调整工作、短休、长休时间，以及在自动/手动模式间切换。可一键保存设置并刷新计时。  
   - **ProgressScreen**：整合多种统计数据与图表，为用户提供直观的生产力分析。

   UI 基于 Material3 风格，通过 StateFlow 实现实时更新，界面随数据变化自动刷新，无需手动干预。

7. **生命周期与后台服务整合**：  
   - **MainActivity** 展示了绑定 **TimerService** 的示例，后续可扩展为在后台计时，确保应用在前台不可见时仍能正常运行（如有需要）。  
   - **TaskActivity**、**ProgressActivity** 等演示了在不同界面中初始化各自的 Controller 并设置对应 UI 内容，实现模块化设计。



## 运行说明

1. 使用 Android Studio 打开项目。  
2. 确保已安装最新的 Kotlin、Compose 及 Gradle 环境。  
3. 在本地设备或模拟器中运行应用。  
4. 首次进入主界面可选择（或新建）任务，然后启动定时器。可通过底部导航切换到设置或进度界面查看统计与图表。

## 后续改进

- 增加通知或前台服务，在应用后台运行时进行提醒。  
- 完善统计数据的过滤与分析功能，助力用户深入了解长期工作趋势。  
- 若有需要，可加入用户账户体系和数据同步功能，实现多设备共享进度。

## 后续改进

- 增加通知或前台服务，在应用后台运行时进行提醒。  
- 完善统计数据的过滤与分析功能，助力用户深入了解长期工作趋势。  
- 若有需要，可加入用户账户体系和数据同步功能，实现多设备共享进度。

- ## 后续改进

- 增加通知或前台服务，在应用后台运行时进行提醒。  
- 完善统计数据的过滤与分析功能，助力用户深入了解长期工作趋势。  
- 若有需要，可加入用户账户体系和数据同步功能，实现多设备共享进度。

- ## 后续改进

- 增加通知或前台服务，在应用后台运行时进行提醒。  
- 完善统计数据的过滤与分析功能，助力用户深入了解长期工作趋势。  
- 若有需要，可加入用户账户体系和数据同步功能，实现多设备共享进度。

- ## 后续改进

- 增加通知或前台服务，在应用后台运行时进行提醒。  
- 完善统计数据的过滤与分析功能，助力用户深入了解长期工作趋势。  
- 若有需要，可加入用户账户体系和数据同步功能，实现多设备共享进度。

- ## 后续改进

- 增加通知或前台服务，在应用后台运行时进行提醒。  
- 完善统计数据的过滤与分析功能，助力用户深入了解长期工作趋势。  
- 若有需要，可加入用户账户体系和数据同步功能，实现多设备共享进度。

- ## 后续改进

- 增加通知或前台服务，在应用后台运行时进行提醒。  
- 完善统计数据的过滤与分析功能，助力用户深入了解长期工作趋势。  
- 若有需要，可加入用户账户体系和数据同步功能，实现多设备共享进度。

- ## 后续改进

- 增加通知或前台服务，在应用后台运行时进行提醒。  
- 完善统计数据的过滤与分析功能，助力用户深入了解长期工作趋势。  
- 若有需要，可加入用户账户体系和数据同步功能，实现多设备共享进度。

- ## 后续改进

- 增加通知或前台服务，在应用后台运行时进行提醒。  
- 完善统计数据的过滤与分析功能，助力用户深入了解长期工作趋势。  
- 若有需要，可加入用户账户体系和数据同步功能，实现多设备共享进度。

- ## 后续改进

- 增加通知或前台服务，在应用后台运行时进行提醒。  
- 完善统计数据的过滤与分析功能，助力用户深入了解长期工作趋势。  
- 若有需要，可加入用户账户体系和数据同步功能，实现多设备共享进度。

- ## 后续改进

- 增加通知或前台服务，在应用后台运行时进行提醒。  
- 完善统计数据的过滤与分析功能，助力用户深入了解长期工作趋势。  
- 若有需要，可加入用户账户体系和数据同步功能，实现多设备共享进度。

- ## 后续改进

- 增加通知或前台服务，在应用后台运行时进行提醒。  
- 完善统计数据的过滤与分析功能，助力用户深入了解长期工作趋势。  
- 若有需要，可加入用户账户体系和数据同步功能，实现多设备共享进度。

- ## 后续改进

- 增加通知或前台服务，在应用后台运行时进行提醒。  
- 完善统计数据的过滤与分析功能，助力用户深入了解长期工作趋势。  
- 若有需要，可加入用户账户体系和数据同步功能，实现多设备共享进度。

- ## 后续改进

- 增加通知或前台服务，在应用后台运行时进行提醒。  
- 完善统计数据的过滤与分析功能，助力用户深入了解长期工作趋势。  
- 若有需要，可加入用户账户体系和数据同步功能，实现多设备共享进度。

- ## 后续改进

- 增加通知或前台服务，在应用后台运行时进行提醒。  
- 完善统计数据的过滤与分析功能，助力用户深入了解长期工作趋势。  
- 若有需要，可加入用户账户体系和数据同步功能，实现多设备共享进度。

- ## 后续改进

- 增加通知或前台服务，在应用后台运行时进行提醒。  
- 完善统计数据的过滤与分析功能，助力用户深入了解长期工作趋势。  
- 若有需要，可加入用户账户体系和数据同步功能，实现多设备共享进度。
