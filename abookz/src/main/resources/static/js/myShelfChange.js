const changeButtonSmall = document.getElementById('smallButton');
const changeButtonLarge = document.getElementById('largeButton');
const myShelfDiv =document.getElementById('myShelfDiv');
const myShelfWrap =document.getElementById('listWrap');
myShelfWrap.style.display='none';
let isChangeCheckButton =false;
window.onload = function() {
    let isChangeCheckButton = localStorage.getItem('isChangeCheckButton') === 'true';
    if (isChangeCheckButton) {
        myShelfWrap.style.display = 'block';
        myShelfDiv.style.display = 'none';
    } else {
        myShelfWrap.style.display = 'none';
        myShelfDiv.style.display = 'block';
    }
};

changeButtonSmall.addEventListener('click', function () {
    myShelfWrap.style.display = 'none';
    myShelfDiv.style.display = 'block';
    localStorage.setItem('isChangeCheckButton', 'false');
});

changeButtonLarge.addEventListener('click', function () {
    myShelfWrap.style.display = 'block';
    myShelfDiv.style.display = 'none';
    localStorage.setItem('isChangeCheckButton', 'true');
});

changeButtonSmall.addEventListener('click', function (){

    if(isChangeCheckButton) {
        myShelfWrap.style.display='none';
    }
    myShelfDiv.style.display='block';
    isChangeCheckButton = false;
})

changeButtonLarge.addEventListener('click', function (){
    if(!isChangeCheckButton){
        myShelfWrap.style.display='block';
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
