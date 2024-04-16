var getIsbn13s = document.getElementsByClassName('getIsbn13');
var getButton =document.getElementsByClassName('myShelfBtn');
console.log(getButton+"button");
console.log(getIsbn13s+ " = GETiSBN13")
for(var i=1; getIsbn13s.length; i++){
        getButton[i].addEventListener('click', function (){

            wantToRead(getIsbn13s[i].getAttribute('value'))
    })
}

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
            alert("이미 등록 되어있습니다");

        });
}