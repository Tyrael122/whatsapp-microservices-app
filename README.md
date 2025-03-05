# WhatsApp Microservices - Android App

## Overview
This is a Kotlin-based Android application that connects to a [RabbitMQ-backed microservices](https://github.com/Tyrael122/whatsapp-microservices) architecture to enable real-time messaging. The app allows users to log in, select chat participants, join chats, and exchange messages instantly.

## Features
- **Simple Login System**: Users enter a username (no password required). If the user doesn’t exist, they are automatically created.
- **Real-time Messaging**: Uses RabbitMQ to send and receive messages instantly.
- **Chat Management**:
    - Fetches available chats via an API.
    - Users can select which participants will be part of the chat.
- **No Message History**: Focuses on real-time conversations only.

## Architecture
- **Frontend**: Android app written in Kotlin.
- **Backend**: Microservices architecture using RabbitMQ, Kubernetes, and multiple services for user and chat management.
- **Messaging**: Uses RabbitMQ exchanges and queues for efficient message delivery.

## Setup Instructions
1. Clone the repository.
2. Configure the backend service URLs in the app.
3. Ensure RabbitMQ and the microservices are running.
4. Build and run the application on an Android device or emulator.

## Future Enhancements
- Improve UI/UX.
- Implement push notifications.
- Cloud deployment for scalability.

## Conclusion
This Android app successfully demonstrates a WhatsApp-like messaging experience using RabbitMQ and microservices, enabling seamless real-time communication.

