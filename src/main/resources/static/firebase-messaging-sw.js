importScripts("https://www.gstatic.com/firebasejs/10.12.2/firebase-app-compat.js");
importScripts("https://www.gstatic.com/firebasejs/10.12.2/firebase-messaging-compat.js");

firebase.initializeApp({
  apiKey: "AIzaSyDkYKfMwvD2-RsW2exNrBiRpbJ6EdjWPdc",
  authDomain: "mokuroku-ad999.firebaseapp.com",
  projectId: "mokuroku-ad999",
  storageBucket: "mokuroku-ad999.firebasestorage.app",
  messagingSenderId: "585869115548",
  appId: "1:585869115548:web:f99c882aecd0a3fd66ed59",
  measurementId: "G-WQRW9079RG"
});

const messaging = firebase.messaging();

// 백그라운드(탭이 없거나 비활성) 데이터 메시지 수신 시
messaging.onBackgroundMessage((payload) => {
  const title = payload.notification?.title || payload.data?.title || "Mokuroku";
  const body  = payload.notification?.body  || payload.data?.body  || "";
  const link  = payload.fcmOptions?.link || payload.data?.url || "/";

  self.registration.showNotification(title, {
    body,
    // 필요하면 정적 리소스 아이콘 경로(옵션)
    // icon: "/images/icons/icon-192.png",
    data: { link, ...payload.data }
  });
});

// 클릭으로 열기(전역에 1번만 등록)
self.addEventListener("notificationclick", (event) => {
  event.notification.close();
  const link = event.notification.data?.link || "/";
  const productId = event.notification.data?.productId;
  const finalUrl = productId ? `/products/${productId}` : link;

  event.waitUntil(
      clients.matchAll({ type: "window", includeUncontrolled: true }).then(list => {
        for (const client of list) {
          // 이미 열려 있으면 포커스
          if (client.url.includes(finalUrl) && "focus" in client) return client.focus();
        }
        return clients.openWindow(finalUrl);
      })
  );
});
