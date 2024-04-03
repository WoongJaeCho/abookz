 function wantToRead (isbn){
    var isbn13 = isbn;
   fetch('/want?book=' + isbn13,{
    method: 'POST',
    headers: {
     'Content-Type' : 'application/json'
    },
    body: JSON.stringify({isbn13 : isbn13})
   })
       .then(response => {
           if (!response.ok) {
               throw new Error('Network response was not ok');
           }
           return response.text();
       })
       .then(data => {
           alert(data);
       })
       .catch(error => {
           console.error('There was a problem with the fetch operation:', error);
           response.json().then(errorMessage => {
              alert("이미 등록 되어있습니다");
           });
       });
 }