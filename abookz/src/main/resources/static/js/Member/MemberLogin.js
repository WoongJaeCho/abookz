// function login() {
//   const username = document.getElementById('Id').value;
//   const password = document.getElementById('password').value;
//
//   const headers = new Headers({
//     'Content-Type': 'application/json',
//   });
//
//   fetch('/login', {
//     method: 'POST',
//     headers: headers,
//     body: JSON.stringify({ username: username, password: password })
//   })
//       .then(response => response.json())
//       .then(data => {
//         if (data.success) {
//           console.log('Login Success:', data);
//           updateFeedback(data.message || '로그인에 성공하였습니다.', true);
//         } else {
//           throw new Error(data.message || '로그인에 실패하였습니다.');
//         }
//       })
//       .catch(error => {
//         console.error('Login Error:', error);
//         updateFeedback(error.message, false);
//       });
// }
