import{E as h,h as y,r as b,B as s,k as a,m as t,q as _,v as k,x as w,A as l,z as x,N as S,C as B,D as z,O as D}from"./index-CHaRzVd8.js";import{_ as F}from"./_plugin-vue_export-helper-DlAUqK2U.js";const u=r=>(B("data-v-86bbdf45"),r=r(),z(),r),C={class:"d-flex flex-wrap align-items-center justify-content-between px-3 py-2 border-bottom",style:{"background-color":"#FFFFFF"}},I=S('<div class="d-flex align-items-center" data-v-86bbdf45><a href="/archive" data-v-86bbdf45><img src="'+D+'" style="height:3rem;" data-v-86bbdf45></a><ul class="nav ms-1" data-v-86bbdf45><li data-v-86bbdf45><a class="nav-link active archive-link" aria-current="page" style="font-size:1rem;font-weight:550;" href="/archive" data-v-86bbdf45><span class="material-symbols-outlined" style="font-size:1rem;" data-v-86bbdf45>description</span>지식아카이브</a></li><li data-v-86bbdf45><a class="nav-link active drive-link" aria-current="page" style="font-size:1rem;font-weight:550;" href="/drive" data-v-86bbdf45><span class="material-symbols-outlined" style="font-size:1rem;" data-v-86bbdf45>attach_file</span>통합 자료실</a></li></ul></div>',1),$={class:"d-flex align-items-center"},N=u(()=>t("span",{class:"material-symbols-outlined"},"saved_search",-1)),V=[N],H={class:"dropdown"},L={key:0,class:"btn btn-outline-light dropdown-toggle",type:"button","data-bs-toggle":"dropdown","aria-expanded":"false",style:{"--bs-btn-color":"#4b4b4b",color:"black"}},U={class:"dropdown-menu"},E=u(()=>t("li",null,[t("a",{class:"dropdown-item",href:"/mypage"},"나의 정보")],-1)),M={__name:"Header",setup(r){h();const i=y(),o=b(localStorage.getItem("token")),p=b(localStorage.getItem("username")),d=b(""),m=async()=>{console.log(d.value);try{i.push(`/search/${d.value}`)}catch(c){console.error("Failed to navigate:",c)}},v=()=>{localStorage.clear(),i.push("/login")},g=()=>{i.push("/login")},f=()=>{i.push("/signup")};return(c,e)=>(s(),a("header",C,[I,t("div",$,[o.value!==null?(s(),a("form",{key:0,onSubmit:_(m,["prevent"]),class:"w-20 me-1",role:"search"},[k(t("input",{type:"search",class:"form-control",placeholder:"total search","aria-label":"Search","onUpdate:modelValue":e[0]||(e[0]=n=>d.value=n)},null,512),[[w,d.value]])],32)):l("",!0),o.value!==null?(s(),a("button",{key:1,type:"button",class:"btn btn-outline-light me-1",style:{"--bs-btn-padding-y":".5rem","--bs-btn-padding-x":".5rem","--bs-btn-font-size":".9rem",color:"#7248BD"},onClick:e[1]||(e[1]=n=>c.Search())},V)):l("",!0),o.value===null?(s(),a("button",{key:2,type:"button",class:"btn btn-outline-light me-2 btn-sm",style:{"--bs-btn-padding-y":".5rem","--bs-btn-padding-x":".5rem","--bs-btn-font-size":".9rem","border-color":"#7248BD",color:"#7248BD"},onClick:e[2]||(e[2]=n=>f())},"Sign Up")):l("",!0),o.value===null?(s(),a("button",{key:3,type:"button",class:"btn btn-outline-light text-white btn-sm",style:{"--bs-btn-padding-y":".5rem","--bs-btn-padding-x":".5rem","--bs-btn-font-size":".9rem","border-color":"#7248BD","background-color":"#7248BD"},onClick:e[3]||(e[3]=n=>g())},"Login")):l("",!0),t("div",H,[o.value!==null?(s(),a("button",L,x(p.value),1)):l("",!0),t("ul",U,[E,t("li",null,[t("a",{class:"dropdown-item",onClick:e[4]||(e[4]=n=>v())},"로그아웃")])])])])]))}},q=F(M,[["__scopeId","data-v-86bbdf45"]]);export{q as H};