import { createRouter, createWebHistory } from "vue-router";
import MemberSignUp from "@/views/MemberSignUp.vue";
import LoginPage from "@/views/LoginPage.vue";
import MemberList from "@/views/MemberList.vue";

const routes = [
  {
    path: "/member/sign-up",
    name: "MemberSignUp",
    component: MemberSignUp,
  },
  {
    path: "/login",
    name: "LoginPage",
    component: LoginPage,
  },
  {
    path: "/member/list",
    name: "MemberList",
    component: MemberList,
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;
