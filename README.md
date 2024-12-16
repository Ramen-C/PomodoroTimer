## 1. References

- [spring-projects/spring-boot: Spring Boot helps you to create Spring-powered, production-grade applications and services with absolute minimum fuss.](https://github.com/spring-projects/spring-boot)
- [Android Studio Ladybug | 2024.2.1 | Android Developers](https://developer.android.google.cn/studio/releases?hl=en)
- [Spring Boot](https://spring.io/projects/spring-boot)

---

## 2. Overall Description

### 2.1 Product Perspective

The Pomodoro Timer application is a standalone mobile app designed to help users improve productivity through time management. The system provides features such as timers, task management, and progress statistics, enabling users to better allocate and track their time. The application is designed around users' task and time management needs, adopting modern architectures and technology stacks to ensure high performance and maintainability. Users can select or create tasks and complete them through set Pomodoro Technique cycles. Additionally, the app records users' time distribution and working hours on different tasks, helping them reasonably allocate time among multiple tasks.

### 2.2 Product Functions

The main features of the application include:

- **Timer Module**:
  - Provides timer functionality, supporting user-customized work time, short break time, and long break time. The timer supports start, pause, resume, and stop operations and supports automatic looping of Pomodoro Technique cycles, reducing manual operations.
  - When the timer ends, the app reminds the user through the device's notification system.

- **Task Module**:
  - Allows users to create, edit, and delete tasks, supporting task classification, sorting, and searching, making it convenient to manage a large number of tasks.
  - Users can choose the default task "Study" or create custom tasks (e.g., "Programming," "Reading").
  - After selecting a task, the timer will be associated with that task, facilitating progress statistics and time allocation.

- **Progress Summary Module**:
  - Records the user's working hours and completed Pomodoro counts on each task, visually displaying time allocation and efficiency through bar charts, pie charts, and other graphs.
  - Supports viewing progress statistics over daily, weekly, and monthly time ranges, helping users track long-term time management effectiveness.

- **Data Synchronization**:
  - When the user is online, the system synchronizes the user's tasks and progress data to the backend server, enabling cross-device data sharing and backup.

### 2.3 User Classes and Characteristics

The application is primarily intended for users with time management needs, suitable for anyone who wants to improve productivity through task allocation and time tracking, such as students, professionals, freelancers, etc. Users do not need technical knowledge to use the application.

### 2.4 Operating Environment

The application runs on smart mobile devices with the Android operating system, supporting Android 8.0 and above. Users can use the app in various environments, including workplaces, study areas, and homes. The app supports both online and offline modes; when the network connection is restored, data will be automatically synchronized.

### 2.5 User Environment

- **Frontend Development**:
  - **Programming Language and Framework**: Uses Kotlin language, adopting the MVVM (Model-View-ViewModel) architectural pattern, and leverages Android Jetpack components (such as ViewModel, LiveData, Data Binding, Navigation, etc.) for development, improving code maintainability and extensibility.
  - **Dependency Injection**: Utilizes Hilt for dependency injection, simplifying object creation and management, enhancing code modularity and testability.
  - **Interface Design**: Follows Material Design guidelines, adopting responsive design to adapt to different screen sizes, including smartphones and tablets.
  - **Internationalization and Localization**: Supports multiple languages, following Android's internationalization and localization design principles to expand the user base.

- **Local Data Storage**:
  - **Database**: Uses the Room persistence library based on SQLite to achieve local data persistence.
  - **Encrypted Storage**: Encrypts sensitive local data to enhance data security.
  - **Data Synchronization Strategy**: Employs incremental synchronization and background synchronization mechanisms to optimize data synchronization efficiency and reliability.

- **Backend Development**:
  - **Framework and Tech Stack**: Utilizes the Spring Boot framework to provide RESTful API services, supporting synchronization of tasks and progress data.
  - **API Design**: Follows RESTful conventions and implements API versioning to facilitate future updates and maintenance.
  - **Security and Authentication**: Uses JWT (JSON Web Token) for user authentication, ensuring secure API access. All data transmissions are encrypted via HTTPS protocol to safeguard data security.

- **Network Communication**:
  - **Protocol and Data Format**: Data transmission between the frontend and backend uses the HTTPS protocol, with JSON as the data format, ensuring secure and efficient data transfer.
  - **Timeout and Retry Mechanisms**: Sets timeout and retry strategies in network requests to enhance application availability under unstable network conditions.

- **Performance and Efficiency**:
  - **Asynchronous Processing**: Utilizes Kotlin coroutines to handle asynchronous tasks, avoiding blocking the main thread and improving application responsiveness.
  - **Resource Optimization**: Optimizes the size and loading methods of images, animations, and other resources to reduce application size and memory usage, enhancing loading speed.
  - **Memory Management**: Uses memory leak detection tools like LeakCanary to prevent memory leaks, improving application stability.

### 2.6 Assumptions and Dependencies

- **Network Connection**: Assumes that the user's device has a stable network connection for real-time data synchronization. However, the app also supports offline mode to ensure normal use without network connectivity.
- **Cloud Service Dependency**: The system relies on third-party cloud service providers to host backend servers and databases, ensuring data persistence and high availability.
- **User Behavior Assumptions**: Assumes that users will select or create tasks before starting the timer so that the system can associate timing data with specific tasks, ensuring the accuracy of progress statistics.
- **Third-party Libraries and Services**: The application depends on Android Jetpack components, Kotlin coroutines, Hilt, Firebase Crashlytics, and other third-party libraries and services to enhance development efficiency and application quality.

---

## 3. External Interface Requirements

### 3.1 User Interfaces

- **Main Interface (MainActivity)**: Provides the core functionality of the Pomodoro Timer, including Start, Pause, Resume, and Stop buttons. The interface displays the countdown timer, the name of the current task, and the number of Pomodoro cycles completed.

    - **Select Task Button**: Allows users to select or change tasks before starting the timer.
    - **Timer Display Area**: Dynamically shows the remaining time and indicates the current status (working or resting) through colors or icons.
    - **Lock Screen and Floating Window Support**: Allows users to view the timer on the lock screen or as a floating window.

- **Task Management Interface (TaskActivity)**: Used for creating, editing, selecting, and managing tasks.

    - **Task List**: Displays the list of tasks the user has created, allowing selection of a task to associate with the timer.
    - **New Task Button**: Users can create new tasks, providing a name and optional description or category.
    - **Edit and Delete Options**: Allows users to edit task details or delete tasks.

- **Progress View Interface (ProgressActivity)**: Provides statistical data on work and rest, helping users track time allocation across different tasks and time periods.

    - **Statistical Data Display**: Shows the number of completed Pomodoro cycles, total duration per task, and progress statistics by day/week/month.
    - **Chart Display**: Visually displays time allocation using bar charts, pie charts, or line graphs.

- **Settings Interface (SettingsActivity)**: Provides configuration options for timer parameters and application preferences.

    - **Work and Rest Time Settings**: Allows users to customize the durations of work time, short breaks, and long breaks.
    - **Cycle Settings**: Users can set the number of cycles before a long break.
    - **Account Settings**: If user accounts are supported, this section allows users to manage account information and synchronization preferences.

- **User Authentication Interface (LoginActivity and RegistrationActivity)** (Potential Implementation):

    - **Login Interface**: Allows existing users to log in using credentials.
    - **Registration Interface**: Supports new users in creating accounts via email and password or through third-party services.
    - **Password Recovery**: Provides options to recover forgotten passwords.

### 3.2 Hardware Interfaces

- **Android Device Hardware**:

    - **Touch Screen**: Used for user interactions, including tapping buttons and navigating the application.
    - **Local Storage**: Utilizes the device's storage (SQLite database) to save user data locally.
    - **Notification System**: Uses the device's notification system to remind users when the timer ends.
    - **Battery and Power Management**: Optimizes application performance to minimize power consumption.

- **Sensors and Permissions**:

    - **Do Not Disturb Mode Access**: Requests permission, if necessary, to override Do Not Disturb settings for critical notifications.
    - **Foreground Service**: Uses foreground service functionality to ensure the timer runs reliably in the background.

### 3.3 Software Interfaces

- **Local Database (using Room and SQLite)**: Used for local persistent storage of tasks, progress, timer data, and user settings, ensuring functionality when offline.

    - **Database Table Structure**:
        - **User Table**: Stores user account information if accounts are supported.
        - **Task Table**: Stores task details, including name, category, creation time, and user ID (if applicable).
        - **Progress Table**: Records completed Pomodoro cycles, task ID, timestamps, and durations.
        - **Settings Table**: Saves user preferences for timer configurations, themes, notifications, etc.

- **Backend API (RESTful API using Spring Boot)**: Facilitates data communication between the application and the backend server, supporting synchronization and user account management.

    - **Authentication API**: Uses secure authentication mechanisms (e.g., JWT) to handle user registration, login, logout, and token refresh.
    - **Task API**: Supports creation, updating, deletion, and querying of tasks, with appropriate authorization checks.
    - **Progress API**: Provides endpoints for uploading and retrieving progress data, supporting queries by task and time period.
    - **Settings API**: Supports cross-device synchronization of user settings, if necessary.
    - **Error Handling**: Returns standardized error responses and appropriate HTTP status codes.

- **Third-Party Services**:

    - **Analytics Services**: Integrates services like Firebase Analytics to track application usage (optional and respecting user privacy).
    - **Crash Reporting**: Uses tools like Firebase Crashlytics to monitor application stability.

### 3.4 Communication Protocols and Interfaces

- **HTTPS Protocol**: All communication between the application and the backend server uses HTTPS to ensure secure data transmission.

- **Authentication Tokens**: Uses JWT or similar tokens for authenticated requests, included in the HTTP headers.

- **Data Format**: Data exchanged between the application and the server uses JSON format for easy parsing and serialization.

- **Synchronization Strategy**:

    - **Background Synchronization**: Schedules background data synchronization tasks using WorkManager or similar tools.
    - **Incremental Synchronization**: Only transmits data that has changed since the last synchronization to optimize network usage.
    - **Conflict Resolution**: Implements strategies to handle data conflicts during synchronization.

- **Network Error Handling**:

    - **Timeouts and Retries**: Configures appropriate timeouts and retry mechanisms for network requests.
    - **Offline Mode**: Provides friendly messages or indicators when the network is unavailable.

- **Push Notifications**:

    - **FCM Integration**: Integrates Firebase Cloud Messaging for real-time notifications or updates, if applicable.

---
## 4. System Features

### 4.1 Feature: Pomodoro Timer

#### 4.1.1 Description and Priority

The Pomodoro Timer is the core functional module of the application, allowing users to set and start timers. By customizing work and rest times, it facilitates cyclical timing to enhance focus and work efficiency. This feature is of the highest priority.

#### 4.1.2 Action/Result

- Users set the durations for work time, short breaks, and long breaks, as well as the number of cycles before a long break, then start the timer.
- The system begins the countdown and reminds the user to rest or continue to the next cycle at the end of each period.
- The timer supports start, pause, resume, and stop operations.
- Users can view the remaining time, current task name, and number of Pomodoro cycles completed during the timing process.
- The system supports displaying the timer on the lock screen or as a floating window, making it convenient for users to check at any time.

#### 4.1.3 Functional Requirements

- **FR1**: Users can customize the durations of work time, short breaks, and long breaks, as well as the number of cycles before a long break.
- **FR2**: The timer should support start, pause, resume, and stop operations to ensure users have control over the timing process.
- **FR3**: During the countdown, the system should update the UI in real-time, displaying the remaining time, current status (working or resting), and the number of cycles completed.
- **FR4**: When the timer ends, the system triggers customizable notifications (including sound, vibration, etc.) to remind users to rest or enter the next cycle.
- **FR5**: Support displaying the timer on the lock screen and as a floating window to enhance user convenience.
- **FR6**: When the timer runs in the background, it should ensure stability and not be affected by system process cleaning.

### 4.2 Feature: Task Management

#### 4.2.1 Description and Priority

The Task Management module allows users to create, select, and manage tasks to associate with the timer function, recording focused time for each task. This feature is of high priority, ensuring users can reasonably allocate and track their time.

#### 4.2.2 Action/Result

- Users can view the task list, select existing tasks, or create new tasks.
- Users can edit task details or delete tasks that are no longer needed.
- Before starting the timer, users need to select a task; the system defaults to the task "Study."
- The system associates the timer with the selected task for subsequent statistics.

#### 4.2.3 Functional Requirements

- **FR1**: Users can create new tasks, specify names, and optionally add descriptions or categories.
- **FR2**: Users can view the task list and select a task as the current timing task.
- **FR3**: The system provides a default task "Study," which is associated by default if the user does not select a task.
- **FR4**: Users can edit task information, including name, description, and category.
- **FR5**: Users can delete tasks; after deletion, the system should update the task list and handle related data associations.

### 4.3 Feature: Progress Tracking

#### 4.3.1 Description and Priority

The Progress Tracking module records the user's working time and the number of Pomodoro cycles completed for each task. This module is of medium priority, helping users understand their time allocation and work efficiency.

#### 4.3.2 Action/Result

- The system records task completion details at the end of each Pomodoro cycle, including timestamp, task ID, and duration.
- Users can view the total duration and number of Pomodoros completed for specific tasks, as well as statistics by day, week, and month.
- The system provides visual charts to intuitively display time allocation and work efficiency.

#### 4.3.3 Functional Requirements

- **FR1**: The system records the completion details of each Pomodoro cycle, saving the start and end times, associated task, and duration.
- **FR2**: Users can view total working time and Pomodoro counts for specific tasks and all tasks.
- **FR3**: The system supports statistical tracking of task progress over time ranges such as day, week, and month, and provides a time range selection function.
- **FR4**: Progress data visualization, offering bar charts, pie charts, or line graphs to display time allocation and work efficiency.
- **FR5**: Users can export progress data or share it via social media to promote self-motivation and interaction.

### 4.4 Feature: Data Synchronization

#### 4.4.1 Description and Priority

The Data Synchronization feature allows users to synchronize tasks and progress data to the backend server when connected to the network, ensuring secure data storage and multi-device access. This feature is of high priority, guaranteeing the integrity and availability of user data.

#### 4.4.2 Action/Result

- When the user is online, the system automatically or in the background synchronizes tasks and progress data to the backend server.
- Users can also manually trigger data synchronization to ensure the data is up-to-date.
- During synchronization, the system handles possible data conflicts to ensure data consistency.
- After synchronization is complete, the system displays information about the success or failure of the synchronization on the interface.

#### 4.4.3 Functional Requirements

- **FR1**: The system automatically detects the network status and, when the network is available, uses background synchronization strategies to automatically synchronize local data to the backend server.
- **FR2**: Users can manually trigger data synchronization to immediately update the latest tasks and progress data.
- **FR3**: During synchronization, the system transmits data in JSON format to the backend server through secure APIs (using HTTPS and JWT authentication) to ensure data integrity and security.
- **FR4**: The system implements incremental synchronization, transmitting only data that has changed since the last synchronization to optimize network usage and synchronization efficiency.
- **FR5**: The system should handle data conflicts during synchronization and adopt appropriate strategies (such as prioritizing the most recent modifications) to resolve conflicts.
- **FR6**: After synchronization is complete, the system should prompt the synchronization status on the interface, such as success or failure, and provide necessary error information.
- **FR7**: When the network is unavailable, the system should prompt the user and support offline mode, automatically synchronizing data when the network is restored.
---
## 5. Other Nonfunctional Requirements

### 5.1 Performance Requirements

- **Timer Stability**: The timer must be stable when running in the foreground and background, ensuring it will not be interrupted due to app switching, background running, or system process cleanup. The timer should use a foreground service to guarantee accurate timing under all circumstances.
    
- **Response Speed**: When users set times, select tasks, view progress, etc., the system response time should not exceed 0.5 seconds, ensuring smooth operation and interface transitions.
    
- **Data Synchronization Performance**: Under good network connectivity, synchronization of tasks and progress data should be completed within 3 seconds. The synchronization process should occur in the background, not affecting users' normal operations.
    
- **Data Persistence**: After abnormal closure, crash, or device restart, the application should recover the user's last timing state and progress records, ensuring no data loss. Data should be saved promptly after critical operations.
    

### 5.2 Safety Requirements

- **Data Backup and Recovery**: Data synchronization must ensure safe transmission of task and progress information, avoiding data loss or corruption. In unstable network conditions, the system should provide breakpoint resume functionality, ensuring data integrity.
    
- **Resource Usage Optimization**: The timer and data synchronization processes should minimize battery, power, and memory consumption, optimizing application performance and avoiding impacts on the device's normal use and other applications' operation.
    
- **Crash Recovery**: In the event of a system crash or abnormal closure, the application needs to record unfinished Pomodoro cycles. Upon restart, it should be able to recover timing progress or prompt the user to continue the task.
    

### 5.3 Security Requirements

- **Encrypted Data Storage**: All sensitive data stored locally, such as tasks, progress, and user settings, should be encrypted, preventing unauthorized access and data leakage.
    
- **Secure Communication**: All data transmissions between the application and the backend server must use HTTPS protocol, ensuring the confidentiality and integrity of data during transmission. Use SSL/TLS encryption to prevent man-in-the-middle attacks.
    
- **Authentication and Authorization**: If user accounts are supported, each user's data must be isolated from other users, ensuring that a user's tasks and progress data are accessible only by themselves. Use secure authentication mechanisms (such as JWT) to manage user sessions.
    
- **Input Validation and Security Checks**: The backend server must perform strict input validation on all received data, preventing security vulnerabilities like SQL injection and cross-site scripting.
    

### 5.4 Software Quality Attributes

- **Reliability**: The application should strictly implement functional requirements, ensuring the accuracy and stability of timing, task management, progress tracking, and data synchronization functions. Critical functional modules should undergo thorough testing to reduce crashes and errors.
    
- **Usability**: The application interface should be simple and intuitive, following user experience design principles. Users should be able to easily complete task selection, timing settings, and progress viewing without guidance. Support for multiple languages and accessibility design should be provided to meet different users' needs.
    
- **Maintainability**: Adopt modular and extensible architecture design, with clear code structure, following coding standards, facilitating future maintenance and function expansion. Use version control and code reviews to improve code quality.↳
    
- **Scalability**: The system design should support the addition of new features (such as advanced statistics, social sharing, third-party integration) in the future, ensuring the application can flexibly expand to meet future needs.↳
    
- **Efficiency**: Optimize the application's performance, including startup time, memory usage, and battery consumption. Use asynchronous operations and optimized algorithms to ensure the application runs efficiently.↳
    

    

### 5.5 Project Documentation

- **Development Documentation**: The development team needs to provide detailed design documents, including system architecture, module function descriptions, data models, and API interface documents, to facilitate later maintenance and onboarding of new members.↳
    
- **Testing Documentation**: Should include detailed test plans and test cases, including functional testing, performance testing, and security testing, ensuring system functions meet requirements and run stably.↳
    
- **Deployment Documentation**: Provide deployment steps, configuration requirements, and considerations for the backend server, as well as packaging and release processes for the frontend application, to facilitate system launch, updates, and migration.↳
    
- **User Manual**: Write a user manual and usage guide, including introductions to the application's main functions, operation steps, frequently asked questions (FAQ), helping users quickly get started and solve problems.↳
    

### 5.6 User Documentation

- **Built-in Help and Tutorials**: Provide in-app beginner guides, help pages, or interactive tutorials to help users understand the application's functions and usage methods.↳
    
- **Feedback and Support**: Provide feedback entry or contact information, allowing users to submit opinions, suggestions, or report issues. Respond to user feedback promptly to enhance user satisfaction.↳
    
- **Privacy Policy and User Agreement**: Clearly display the privacy policy and user agreement in the application, informing users of how their data is used, stored, and protected, ensuring users are informed and agree.

# 软件需求规格说明书（SRS）——番茄钟应用程序

## 1 References

- [spring-projects/spring-boot: Spring Boot helps you to create Spring-powered, production-grade applications and services with absolute minimum fuss.](https://github.com/spring-projects/spring-boot)
- [Android Studio Ladybug | 2024.2.1  |  Android Developers](https://developer.android.google.cn/studio/releases?hl=en)
- [Spring Boot](https://spring.io/projects/spring-boot)
---

## 2. Overall Description

### 2.1 Product Perspective

番茄钟应用程序是一款独立的移动应用，旨在通过时间管理帮助用户提高生产力。系统通过提供计时器、任务管理和进度统计等功能，使用户能够更好地分配和跟踪时间。应用程序围绕用户的任务和时间管理需求设计，采用现代化的架构和技术栈，确保应用的高性能和可维护性。用户可以选择或创建任务，并通过设定的番茄工作法周期完成任务。此外，应用会记录用户在不同任务上的时间分布和工作时长，帮助用户合理安排多个任务的时间。
### 2.2 Product Functions

应用的主要功能包括：
#### 计时器模块：
 提供计时器功能，支持用户自定义工作时间、短休息时间和长休息时间。计时器支持开始、暂停、恢复和停止操作，并支持自动循环番茄工作法周期，减少用户手动操作。
 当计时结束时，应用通过设备的通知系统提醒用户。
#### **任务模块**：
允许用户创建、编辑和删除任务，支持对任务进行分类、排序和搜索，方便管理大量任务。
用户可以选择默认任务“学习”或创建自定义任务（如“编程”、“阅读”）。
选定任务后，计时器将与该任务关联，便于统计进度和时间分配。
#### **进度总计模块**：
记录用户在每个任务上的工作时长和完成的番茄次数，通过柱状图、饼状图等图表直观显示各任务的时间分配和效率。
支持按天、周、月等时间范围查看进度统计，帮助用户跟踪长期的时间管理效果。
#### **数据同步**：
当用户在线时，系统会将用户的任务和进度数据同步到后端服务器，实现跨设备的数据共享和备份。

### 2.3 User Classes and Characteristics

应用主要面向有时间管理需求的用户，适合所有需要通过任务分配和时间跟踪提高生产力的人群，例如学生、工作者、自由职业者等。用户不需要具备技术知识即可使用该应用。

### 2.4 Operating Environment

应用运行于Android操作系统的智能移动设备，支持Android 8.0及以上版本。用户可在各种环境下使用该应用，包括工作场所、学习场所和家庭等。应用支持在线和离线模式；当网络连接恢复后，数据将自动同步。
### 2.5 User Environment

#### **前端开发**：
- **编程语言和框架**：使用Kotlin语言，采用MVVM（Model-View-ViewModel）架构模式，利用Android Jetpack组件（如ViewModel、LiveData、Data Binding、Navigation等）进行开发，提高代码的可维护性和扩展性。

- **依赖注入**：使用Hilt进行依赖注入，简化对象的创建和管理，提升代码的模块化和测试性。

- **界面设计**：遵循Material Design设计规范，采用响应式设计，适配不同尺寸的屏幕，包括手机和平板电脑。

- **国际化和本地化**：支持多语言，遵循Android国际化和本地化设计原则，扩大用户群体。

#### **本地数据存储**：
- **数据库**：使用Room持久化库，基于SQLite数据库，实现本地数据的持久化存储。

- **加密存储**：对本地敏感数据进行加密，提升数据安全性。

- **数据同步策略**：采用增量同步和后台同步机制，优化数据同步的效率和可靠性。

#### **后端开发**：
- **框架和技术栈**：使用Spring Boot框架，提供RESTful API服务，支持任务和进度数据的同步。

- **API设计**：遵循RESTful规范，实施API版本控制，方便未来的更新和维护。

- **安全和身份验证**：使用JWT（JSON Web Token）进行用户身份验证，确保API的安全访问。所有数据传输通过HTTPS协议加密，保障数据的安全性。

- **网络通信**：
    - **协议和数据格式**：前后端之间的数据传输使用HTTPS协议，数据格式采用JSON，确保数据的安全和高效传输。
    - **超时和重试机制**：在网络请求中设置超时和重试策略，提升应用在网络不稳定情况下的可用性。

- **性能和效率**
    - **异步处理**：利用Kotlin协程处理异步任务，避免阻塞主线程，提升应用的响应速度。
    - **资源优化**：优化图片、动画等资源的大小和加载方式，减少应用体积和内存占用，提高加载速度。
    - **内存管理**：使用内存泄漏检测工具，如LeakCanary，防止内存泄漏，提升应用的稳定性。
### 2.6 Assumptions and Dependencies

- **网络连接**：假设用户设备具备稳定的网络连接，以便进行数据的实时同步。但应用也支持离线模式，保证用户在无网络环境下的正常使用。
- **云服务依赖**：系统依赖第三方云服务提供商托管后端服务器和数据库，确保数据的持久化和应用的高可用性。
- **用户操作习惯**：假设用户会在开始计时前选择或创建任务，以便系统将计时数据关联到特定任务，确保进度统计的准确性。
- **第三方库和服务**：应用依赖于Android Jetpack组件、Kotlin协程、Hilt、Firebase Crashlytics等第三方库和服务，以提升开发效率和应用质量。

---

## 3. External Interface Requirements

### 3.1 User Interfaces

- **主界面 (MainActivity)**：提供番茄钟的核心功能，包括开始、暂停、恢复和停止按钮。界面显示倒计时、当前任务名称和已完成的番茄周期次数。
    - **选择任务按钮**：允许用户在开始计时前选择或更改任务。
    - **计时器显示区域**：动态显示剩余时间，并通过颜色或图标指示当前状态（工作中或休息中）。
    - **锁屏和浮动窗口支持**：允许用户在锁屏界面或以浮动窗口的形式查看计时器。
- **任务管理界面 (TaskActivity)**：用于创建、编辑、选择和管理任务。
    - **任务列表**：显示用户已创建的任务列表，允许选择一个任务与计时器关联。
    - **新建任务按钮**：用户可以创建新任务，提供名称和可选的描述或分类。
    - **编辑和删除选项**：允许用户编辑任务详情或删除任务。
- **进度查看界面 (ProgressActivity)**：提供工作和休息的统计数据，帮助用户跟踪不同任务和时间段的时间分配。
    - **统计数据显示**：显示已完成的番茄周期次数、每个任务的总时长、按天/周/月的进度统计。
    - **图表显示**：通过柱状图、饼状图或折线图直观显示时间分配情况。
- **设置界面 (SettingsActivity)**：提供计时器参数和应用偏好的配置选项。
    - **工作和休息时间设置**：允许用户自定义工作时间、短休息和长休息的时长。
    - **循环设置**：用户可以设置在长休息前的循环次数。。
    - **账户设置**：如果支持用户账户，此部分允许用户管理账户信息和同步偏好。

- **用户认证界面 (LoginActivity 和 RegistrationActivity)**（可能会做）：
    - **登录界面**：允许已有用户使用凭证登录。
    - **注册界面**：支持新用户通过邮箱和密码或第三方服务创建账户。
    - **密码恢复**：提供找回密码的选项。

### 3.2 Hardware Interfaces

- **Android设备硬件**：
    
    - **触摸屏**：用于用户交互，包括点击按钮和导航应用。
    - **本地存储**：利用设备的存储（SQLite数据库）本地保存用户数据。
    - **通知系统**：使用设备的通知系统在计时结束时提醒用户。
    - **电池和电源管理**：优化应用性能，最小化电量消耗。
- **传感器和权限**：
    
    - **免打扰模式访问**：如有必要，请求权限以在关键通知时覆盖免打扰设置。
    - **前台服务**：使用前台服务功能，确保计时器在后台可靠运行。

### 3.3 Software Interfaces

- **本地数据库（使用Room和SQLite）**：用于本地持久化存储任务、进度、计时器数据和用户设置，确保离线时的功能性。
    - **数据库表结构**：
        - **用户表**：如果支持账户，存储用户账户信息。
        - **任务表**：存储任务详情，包括名称、分类、创建时间和用户ID（如适用）。
        - **进度表**：记录完成的番茄周期、任务ID、时间戳和时长。
        - **设置表**：保存用户的计时器配置、主题、通知等偏好设置。
- **后端API（使用Spring Boot的RESTful API）**：促进应用与后端服务器之间的数据通信，支持同步和用户账户管理。
    - **认证API**：使用安全的认证机制（如JWT）处理用户注册、登录、注销和令牌刷新。
    - **任务API**：支持任务的创建、更新、删除和查询，具有适当的授权检查。
    - **进度API**：提供上传和获取进度数据的端点，支持按任务和时间段查询。
    - **设置API**：如有必要，支持用户设置的跨设备同步。
    - **错误处理**：返回标准化的错误响应和适当的HTTP状态码。
- **第三方服务**：
    - **分析服务**：集成如Firebase Analytics等服务进行应用使用情况跟踪（可选并尊重用户隐私）。
    - **崩溃报告**：使用如Firebase Crashlytics等工具监控应用稳定性。

### 3.4 Communication Protocols and Interfaces

- **HTTPS协议**：应用与后端服务器之间的所有通信使用HTTPS，确保数据传输的安全。
- **认证令牌**：使用JWT或类似的令牌进行认证请求，包含在HTTP头中。
- **数据格式**：应用与服务器之间交换的数据采用JSON格式，方便解析和序列化。
- **同步策略**：
    - **后台同步**：使用WorkManager或类似工具调度后台数据同步任务。
    - **增量同步**：仅传输自上次同步以来更改的数据，优化网络使用。
    - **冲突解决**：实现策略处理同步期间的数据冲突。
- **网络错误处理**：
    - **超时和重试**：为网络请求配置适当的超时和重试机制。
    - **离线模式**：在网络不可用时提供友好的消息或指示。
- **推送通知**：
    - **FCM集成**：如适用，集成Firebase Cloud Messaging进行实时通知或更新。

---

## 4. System Features

### 4.1 Feature: Pomodoro Timer

#### 4.1.1 Description and Priority

Pomodoro Timer是应用的核心功能模块，允许用户设置和启动计时器，通过自定义的工作和休息时间进行周期性计时，以提升专注力和工作效率。该功能优先级极高。

#### 4.1.2 Action/Result

- 用户设置工作时长、短休息和长休息时长，以及在长休息前的循环次数，然后启动计时器。
- 系统开始倒计时，并在每个周期结束时提醒用户休息或继续下一个周期。
- 计时器支持开始、暂停、恢复和停止操作。
- 用户可在计时过程中查看剩余时间、当前任务名称和已完成的番茄周期次数。
- 系统支持在锁屏界面或以浮动窗口形式显示计时器，方便用户随时查看。

#### 4.1.3 Functional Requirements

- **FR1**：用户可以自定义工作时间、短休息和长休息的时长，以及在长休息前的循环次数。
- **FR2**：计时器应支持开始、暂停、恢复和停止操作，确保用户对计时过程的控制。
- **FR3**：系统在倒计时过程中实时更新UI，显示剩余时间、当前状态（工作或休息）和已完成的循环次数。
- **FR4**：计时结束时，系统触发可自定义的通知（包括声音、振动等）提醒用户休息或进入下一周期。
- **FR5**：支持锁屏界面和浮动窗口显示计时器，提升用户的便捷性。
- **FR6**：计时器在后台运行时，应确保稳定性，不受系统进程清理影响。
### 4.2 Feature: Task Management

#### 4.2.1 Description and Priority

任务管理模块允许用户创建、选择和管理任务，以便与计时器功能关联，记录每个任务的专注时间。该功能优先级高，确保用户可以合理分配和追踪时间。

#### 4.2.2 Action/Result

- 用户可以查看任务列表，选择已有任务或创建新任务。
- 用户可以编辑任务详情或删除不再需要的任务。
- 在开始计时前，用户需要选择任务；系统默认选择任务为“学习”。
- 系统将计时器与选择的任务关联，以便后续统计。
#### 4.2.3 Functional Requirements
- **FR1**：用户可以创建新任务，指定名称，并可选择添加描述或分类。
- **FR2**：用户可以查看任务列表，并选择一个任务作为当前计时任务。
- **FR3**：系统提供默认任务“学习”，在用户未选择任务时默认关联此任务。
- **FR4**：用户可以编辑任务信息，包括名称、描述和分类。
- **FR5**：用户可以删除任务，删除后系统应更新任务列表并处理相关的数据关联。
### 4.3 Feature: Progress Tracking

#### 4.3.1 Description and Priority

进度跟踪模块记录用户在各个任务上的工作时间和完成的Pomodoro周期次数。该模块优先级中等，帮助用户了解自己的时间分配和工作效率。

#### 4.3.2 Action/Result

- 系统在每个番茄周期结束时记录任务完成情况，包括时间戳、任务ID和持续时间。
- 用户可以在进度界面查看特定任务的总时长、已完成的番茄次数，以及按天、周、月的统计数据。
- 系统提供可视化的图表，直观展示时间分配和工作效率。

#### 4.3.3 Functional Requirements

- **FR1**：系统记录每个番茄周期的完成情况，保存开始和结束时间、关联任务和持续时间。
- **FR2**：用户可以查看特定任务和全部任务的工作总时长和番茄次数统计。
- **FR3**：系统支持按天、周、月等时间范围统计任务进度，并提供时间范围选择功能。
- **FR4**：进度数据可视化，提供柱状图、饼状图或折线图展示时间分配和工作效率。
- **FR5**：用户可以导出进度数据，或通过社交媒体分享，促进自我激励和互动。

### 4.4 Feature: Data Synchronization

#### 4.4.1 Description and Priority

数据同步功能允许用户在有网络连接时将任务和进度数据同步到后端服务器，确保数据的安全存储和多设备访问。该功能优先级高，保障用户数据的完整性和可用性。
#### 4.4.2 Action/Result

- 当用户在线时，系统自动或在后台同步任务和进度数据至后端服务器。
- 用户也可以手动触发数据同步，以确保数据最新。
- 在同步过程中，系统处理可能的数据冲突，确保数据一致性。
- 同步完成后，系统在界面上显示同步成功或失败的信息。

#### 4.4.3 Functional Requirements

- **FR1**：系统自动检测网络状态，当网络可用时，使用后台同步策略自动同步本地数据到后端服务器。
- **FR2**：用户可以手动触发数据同步，立即更新最新的任务和进度数据。
- **FR3**：同步过程中，系统通过安全的API（使用HTTPS和JWT认证）将数据以JSON格式传输到后端服务器，确保数据完整性和安全性。
- **FR4**：系统实现增量同步，仅传输自上次同步以来更改的数据，优化网络使用和同步效率。
- **FR5**：系统应处理同步期间的数据冲突，采用适当的策略（如最新修改优先）解决冲突。
- **FR6**：同步完成后，系统在界面上提示同步状态，如成功或失败，并提供必要的错误信息。
- **FR7**：在网络不可用时，系统应提示用户并支持离线模式，待网络恢复后自动同步数据。

---

## 5. Other Nonfunctional Requirements

### 5.1 Performance Requirements

- **计时器稳定性**：计时器在前台和后台运行时必须稳定，确保不会因应用切换、后台运行或系统进程清理而中断。计时器应使用前台服务，保证在各种情况下都能准确计时。
- **响应速度**：用户在设置时间、选择任务、查看进度等操作时，系统响应时间应不超过0.5秒，确保操作流畅、界面切换顺畅。
- **数据同步性能**：在网络连接良好的情况下，任务和进度数据的同步应在3秒内完成。同步过程应在后台进行，不影响用户的正常操作。
- **数据持久性**：应用在异常关闭、崩溃或设备重启后，能够恢复用户的最后计时状态和进度记录，确保数据不丢失。应在关键操作后及时保存数据。

### 5.2 Safety Requirements

- **数据备份和恢复**：数据同步时需确保任务和进度信息的安全传输，避免数据丢失或损坏。在网络不稳定的情况下，系统应提供断点续传功能，确保数据完整性。
- **资源使用优化**：计时器和数据同步过程应尽量减少电池、电量和内存的消耗，优化应用性能，避免影响设备的正常使用和其他应用的运行。
- **崩溃恢复**：在系统崩溃或异常关闭时，应用需记录未完成的番茄周期，重新启动后能够恢复计时进度或提示用户继续任务。
### 5.3 Security Requirements

- **数据加密存储**：所有本地存储的任务、进度和用户设置等敏感数据需进行加密保存，防止未经授权的访问和数据泄露。
- **安全通信**：应用与后端服务器的所有数据传输需使用HTTPS协议，确保数据在传输过程中的机密性和完整性。采用SSL/TLS加密，防止中间人攻击。
- **身份验证和授权**：如支持用户账户，每个用户的数据需与其他用户隔离，确保用户的任务和进度数据仅限本人访问。使用安全的身份验证机制（如JWT），管理用户会话。
- **输入验证和安全检查**：后端服务器需对所有接收的数据进行严格的输入验证，防止SQL注入、跨站脚本等安全漏洞。

### 5.4 Software Quality Attributes

- **可靠性**：应用应严格按照功能需求实现，确保计时、任务管理、进度跟踪和数据同步功能的准确性和稳定性。关键功能模块需经过充分测试，减少崩溃和错误发生。
- **可用性**：应用界面简洁直观，遵循用户体验设计原则，用户可以在无需指导的情况下轻松完成任务选择、计时设置和进度查看。支持多语言和无障碍设计，满足不同用户的需求。
- **可维护性**：采用模块化和可扩展的架构设计，代码结构清晰，遵循编码规范，便于后期的维护和功能扩展。使用版本控制和代码审查，提升代码质量。
- **可扩展性**：系统设计应支持后续添加新功能（如高级统计、社交分享、第三方集成等），确保应用能够灵活扩展以满足未来需求。
- **效率**：优化应用的性能，包括启动时间、内存使用和电池消耗。使用异步操作和优化的算法，确保应用高效运行。


### 5.5 Project Documentation

- **开发文档**：开发团队需提供详细的设计文档，包括系统架构、模块功能说明、数据模型和API接口文档，以便后期维护和新成员上手。
- **测试文档**：应包含详细的测试计划和测试用例，包括功能测试、性能测试和安全测试，确保系统功能符合需求且运行稳定。
- **部署文档**：提供后端服务器的部署步骤、配置要求和注意事项，以及前端应用的打包和发布流程，以便系统上线、更新和迁移。
- **用户手册**：编写用户手册和使用指南，包括应用的主要功能介绍、操作步骤、常见问题解答（FAQ）等，帮助用户快速上手和解决问题。

### 5.6 User Documentation

- **内置帮助和教程**：在应用内提供新手引导、帮助页面或交互式教程，帮助用户了解应用的功能和使用方法。
- **反馈和支持**：提供反馈入口或联系方式，允许用户提交意见、建议或报告问题。及时响应用户反馈，提升用户满意度。
- **隐私政策和用户协议**：在应用中明确展示隐私政策和用户协议，告知用户其数据的使用方式、存储和保护措施，确保用户知情并同意。
