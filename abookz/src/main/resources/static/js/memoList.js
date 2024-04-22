let editBtns= document.getElementsByClassName('edit-btn');
let memoQuotes = document.getElementsByClassName('memo-quotes');
let memoNote = document.getElementsByClassName('memo-note');
let submitBtn =document.getElementsByClassName('submit-btn');
for (let i = 0; i < editBtns.length; i++) {

    editBtns[i].addEventListener('click', function() {
        var memoValue = this.getAttribute('memo-value');
        editBtns[i].classList.add('hidden');
        submitBtn[i].classList.remove('hidden');
        memoQuotes[i].contentEditable = true;
        memoNote[i].contentEditable = true;
        submitBtn[i].addEventListener('click', function() {
        memoQuotes[i].contentEditable = false;
        memoNote[i].contentEditable = false;
        editBtns[i].classList.remove('hidden');
        submitBtn[i].classList.add('hidden');
        let changeMemoQuotes = memoQuotes[i].textContent;
        let changeMemoNote = memoNote[i].textContent;
        console.log(changeMemoNote);
        console.log(changeMemoQuotes);
        updateMemo(memoValue,changeMemoQuotes,changeMemoNote)
        }, {once: true});
    });
}

function updateMemo(id,changeMemo,changeMemoNote){
const  data= {
    id: id,
    quotes :changeMemo,
    note : changeMemoNote
}
    fetch(`/memo/bookShelf/update`, {
    method: "POST",
    headers: {
        'Content-Type': 'application/json'
    },
    body: JSON.stringify(data)
})
        .then(response => response.json())
        .then(data => {
            if (data) {
                alert(data);
            }
        })
        .catch(error => console.error('Error:', error));


}