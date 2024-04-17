var getIsbn13s = document.getElementsByClassName('getIsbn13');
var getButtons =document.getElementsByClassName('bookCategoryBtn');
console.log(getIsbn13s+ " = GETiSBN13")
for(var i=0; i<getButtons.length; i++){
    let getButton = getButtons[i];
    let getIsbn13 = getIsbn13s[i];
    getButton.addEventListener('click', function (){

        wantToReadCategory(getIsbn13)
    })
}

    function wantToReadCategory (isbn){
    let isbn13 = isbn.getAttribute('valueBook');
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
                alert("로그인을 먼저 해주세요");

            });
    }
