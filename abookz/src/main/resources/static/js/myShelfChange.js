const changeButtonSmall = document.getElementById('smallButton');
const changeButtonLarge = document.getElementById('largeButton');
const myShelfDiv =document.getElementById('myShelfDiv');
const myShelfWrap =document.getElementById('myShelfListWrap');
const myShelfPagingButton = document.getElementById('pagingButtonDiv');
myShelfWrap.style.display='none';
let isChangeCheckButton =false;
window.onload = function() {
    let isChangeCheckButton = localStorage.getItem('isChangeCheckButton') === 'true';
    if (isChangeCheckButton) {
        myShelfWrap.style.display = 'block';
        myShelfPagingButton.style.display='block';
        myShelfDiv.style.display = 'none';

    } else {
        myShelfWrap.style.display = 'none';
        myShelfDiv.style.display = 'block';
        myShelfPagingButton.style.display='none';
    }
};

changeButtonSmall.addEventListener('click', function () {
    myShelfWrap.style.display = 'none';
    myShelfPagingButton.style.display='none';
    myShelfDiv.style.display = 'block';
    localStorage.setItem('isChangeCheckButton', 'false');
});

changeButtonLarge.addEventListener('click', function () {
    myShelfWrap.style.display = 'block';
    myShelfPagingButton.style.display='block';
    myShelfDiv.style.display = 'none';
    localStorage.setItem('isChangeCheckButton', 'true');
});

changeButtonSmall.addEventListener('click', function (){

    if(isChangeCheckButton) {
        myShelfWrap.style.display='none';
        myShelfPagingButton.style.display='none';
    }
    myShelfDiv.style.display='block';
    isChangeCheckButton = false;
})

changeButtonLarge.addEventListener('click', function (){
    if(!isChangeCheckButton){
        myShelfWrap.style.display='block';
        myShelfPagingButton.style.display='block';
    }
    myShelfDiv.style.display='none';
    isChangeCheckButton = true;
})
var updateButtonsWrap =  document.getElementsByClassName("myShelf_wrap_update");
var deleteButtonsWrap =  document.getElementsByClassName("myShelf_wrap_delete");
var wrapPopups = document.getElementsByClassName("wrap_popup");
var progressWrapBars = document.getElementsByClassName('progressWrap-bar');
var progressWraps  =document.getElementsByClassName('progressWrap');
var myShelfWrapPages = document.getElementsByClassName('myShelfWrapPage');
for(var i=0; i<wrapPopups.length; i++){
     let  updateButtonWrap =updateButtonsWrap[i];
     let deleteButtonWrap = deleteButtonsWrap[i];
     let wrapPopup =  wrapPopups[i];
     let progressWrapBar = progressWrapBars[i];
    let myShelfWrapPage = myShelfWrapPages[i];
    progressBarCheckWrap(progressWrapBar,myShelfWrapPage)
    deleteButtonWrap.addEventListener("click",() =>{
        if(confirm("정말로 삭제하시겠습니까?")){
            console.log("진입");
            deleteInput(deleteButtonWrap);
        }else{

        }
    })
    updateButtonWrap.addEventListener("click", function() {
        openModal(modal,wrapPopup);
        console.log("모달 열기 버튼 클릭됨");
    });


}
function progressBarCheckWrap(progressBar,myShelfWrapPage){
    let currentPage =myShelfWrapPage.getAttribute('currentPage');
    let itemPage = myShelfWrapPage.getAttribute('itemPage');
    let progress = progressBar.firstChild;
    let progressPercentage = (currentPage / itemPage) * 100;
    progress.style.width = progressPercentage + '%';
    progress.style.backgroundColor = 'pink';
}

let currentPageSlice = 0;
const sizeSlice = 5;
let tagGetAttribute =null ;

    if(document.getElementById('tagGetAttribute')){
        tagGetAttribute =document.getElementById('tagGetAttribute').getAttribute('tag-value');
    }

function loadMoreBooks(){
    currentPageSlice++;
    const url = `/myshelfSlice?page=${currentPageSlice}&size=${sizeSlice}&tag=${tagGetAttribute}`;
    fetch(url,{
    })
.then(response => response.json())
        .then(data => {
            const books = data.content;
            console.log("Is last page:", data.content.last);
            console.log("Is last page:", data.last);
            if (data.last) {
                document.getElementById('moreSlice').style.display = 'none';
            }
            console.log("Loaded books:", books);
            const bookListElement = document.getElementById('myShelfListWrap');
            books.forEach(book => {
                const bookHTML = `
    <div class="card card-side bg-base-100 shadow-xl w-3/4 mb-2 mx-auto">
        <input type="hidden" class="wrap_popup" value="${book.id}"/>
        <figure><img class="w-40" src="${book.bookDTO.cover}" alt="cover"/></figure>
        <div class="card-body">
            <span class="text-center mb-2 font-extrabold text-lg">${book.bookDTO.title.length > 40 ? book.bookDTO.title.substring(0, 40) + '...' : book.bookDTO.title}</span>
            <span>${book.bookDTO.author}</span>
            <div class="progressWrap-bar"></div>
            <span class="myShelfWrapPage">읽은 페이지 : ${book.currentPage ? book.currentPage : '0'} / ${book.bookDTO.itemPage}</span>
        </div>
        <div class="flex flex-col justify-end">
            
            <div class="myShelf_wrap_delete flex-1 m-0" data-value="${book.id}">
                <button class="btn btn-square btn-outline">
                    <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
                    </svg>
                </button>
            </div>
            <div class="shelf_myShelf_review flex-1"><a href="/review/${book.id}/${book.bookDTO.bookId}" class="btn btn-primary btn-review">리뷰</a></div>
            <div class="myShelf_wrap_update flex-1"><button class="btn btn-primary btn-detail">상세보기</button></div>
        </div>
    </div>`;
                bookListElement.insertAdjacentHTML('beforeend', bookHTML);
            });
        })
        .catch(error => {
            console.error('Error loading data:', error);
        });
}