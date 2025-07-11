<template>
  <v-container>
    <v-row justify="center">
      <v-col cols="12" md="8">
        <v-card>
          <v-card-title class="text-h5 text-center"> 채팅 </v-card-title>
          <v-card-text>
            <div class="chat-box">
              <div
                v-for="(msg, index) in messages"
                :key="index"
                :class="[
                  'chat-message',
                  msg.senderEmail === this.senderEmail ? 'sent' : 'received',
                ]"
              >
                <strong>{{ msg.senderEmail }}: </strong> {{ msg.message }}
              </div>
            </div>
            <v-text-field
              v-model="newMessage"
              label="메시지를 입력하세요"
              @keyup.enter="sendMessage"
            />

            <v-btn color="primary" block @click="sendMessage">전송</v-btn>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
import SockJS from "sockjs-client";
import Stomp from "webstomp-client";
//import axios from "axios";

export default {
  data() {
    return {
      messages: [],
      newMessage: "",
      stompClient: null,
      token: "",
      senderEmail: null,
    };
  },
  created() {
    this.senderEmail = localStorage.getItem("email");
    this.connectWebSocket();
  },
  beforeUnmount() {
    // 화면 종료 시
    this.disConnectWebSocket();
  },

  beforeRouteLeave(to, from, next) {
    // 다른 페이지 이동 시
    this.disConnectWebSocket();
    next();
  },

  methods: {
    connectWebSocket() {
      if (this.stompClient && this.stompClient.connected) {
        console.log("이미 WebSocket 연결이 되어 있습니다.");
        return;
      }

      // sockJs를 사용하여 WebSocket 연결을 생성 (http 엔드 포인트 사용)
      const sockJs = new SockJS(`${process.env.VUE_APP_API_BASE_URL}/connect`);
      this.stompClient = Stomp.over(sockJs);
      this.token = localStorage.getItem("token");

      this.stompClient.connect(
        {
          Authorization: `Bearer ${this.token}`, // 토큰을 헤더에 추가
        },
        (frame) => {
          console.log("Connected: " + frame);
          // 연결 성공 시 구독 시작
          this.stompClient.subscribe(`/topic/1`, (message) => {
            const parsedMessage = JSON.parse(message.body);

            // 메세지 구독
            console.log("Received message:", message.body);
            this.messages.push(parsedMessage);
            this.scrollToBottom();
          });
        },
        () => {
          this.stompClient.subscribe(`/topic/1`, (message) => {
            // 메세지 구독
            console.log("Received message:", message.body);
            this.messages.push(message.body);
            this.scrollToBottom();
          });
        }
      );
    },
    sendMessage() {
      if (this.newMessage.trim() === "") {
        return;
      }
      const messageData = {
        message: this.newMessage,
        senderEmail: this.senderEmail, // 메시지 보낸 사람 이메일
      };

      this.stompClient.send(
        `/publish/1`, // 메시지를 보낼 엔드포인트

        JSON.stringify(messageData) // 메시지 내용
      );
      this.newMessage = "";
    },

    scrollToBottom() {
      this.$nextTick(() => {
        const chatBox = this.$el.querySelector(".chat-box");
        chatBox.scrollTop = chatBox.scrollHeight;
      });
    },
    disConnectWebSocket() {
      if (this.stompClient && this.stompClient.connected) {
        this.stompClient.unsubscribe(`/topic/1`);
        this.stompClient.disconnect();
        console.log("WebSocket 연결 해제");
        this.stompClient = null;
      } else {
        console.log("WebSocket 연결이 되어 있지 않습니다.");
      }
    },
  },
};
</script>
<style>
.chat-box {
  height: 300px;
  overflow-y: auto;
  border: 1px solid #ddd;
  margin-bottom: 10px;
}
.chat-message {
  padding: 5px;
  margin-bottom: 10px;
}
.sent {
  text-align: right;
  background-color: #e0f7fa;
  border-radius: 5px;
}
.received {
  text-align: left;
  background-color: #f1f8e9;
  border-radius: 5px;
}
</style>
