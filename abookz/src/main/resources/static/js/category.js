var getIsbn13s = document.getElementsByClassName('getIsbn13');
var getButtons =document.getElementsByClassName('bookCategoryBtn');
console.log(getIsbn13s+ " = GETiSBN13")
for(var i=0; getButtons.length; i++){
    let getButton = getButtons[i];
    let getIsbn13 = getIsbn13s[i];
    getButton.addEventListener('click', function (){

        wantToRead(getIsbn13)
    })
}

    function wantToRead (isbn){
    let isbn13 = isbn.getAttribute('value');
    console.log(isbn13);
    console.log("isbn13"+isbn13);
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
