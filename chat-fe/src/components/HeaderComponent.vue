<template>
  <v-app-bar>
    <v-container>
      <v-row align="center">
        <v-col class="d-flex justify-start">
          <v-btn :to="{ path: '/member/list' }">회원목록</v-btn>
          <v-btn :to="{ path: '/groupchatting/list' }">채팅방 목록</v-btn>
        </v-col>

        <v-col class="text-center">
          <v-btn :to="{ path: '/' }">채팅 서비스</v-btn>
        </v-col>

        <v-col class="d-flex justify-end">
          <v-btn v-if="isLogin" :to="{ path: '/my/chat/page' }"
            >나의 채팅방</v-btn
          >
          <v-btn v-if="!isLogin" :to="{ path: '/member/sign-up' }"
            >회원가입</v-btn
          >
          <v-btn v-if="!isLogin" :to="{ path: '/login' }">로그인</v-btn>
          <v-btn v-if="isLogin" @click="logout">로그아웃</v-btn>
        </v-col>
      </v-row>
    </v-container>
  </v-app-bar>
</template>
<script>
export default {
  name: "HeaderComponent",
  data() {
    return {
      isLogin: false,
    };
  },
  created() {
    const token = localStorage.getItem("token");
    if (token) {
      this.isLogin = true;
    }
  },

  methods: {
    logout() {
      localStorage.clear();
      window.location.reload();
    },
  },
};
</script>
