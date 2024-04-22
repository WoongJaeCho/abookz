
const boardCommentContainer = document.getElementById('boardCommentContainer');
const boardCommentWrite = document.getElementById('boardCommentWrite');
const boardId = document.getElementById('boardCommentList').getAttribute('data-value');
let currentPage = 0;
const pageSize = 10;

fetchComments(boardId, currentPage,pageSize);
function fetchComments(boardId, page,size) {
    console.log("값체크")
    fetch(`/board/comment?boardId=${boardId}&page=${page}&size=${size}`, {
        method: "GET",
        headers: {
            'Content-Type': 'application/json'
        },
    })
        .then(response => response.json())
        .then(data => {
            console.log("데이터값체크" + JSON.stringify(data));
            if (data.content && data.content.length > 0) {
                currentPage = page;
                displayComments(data);
            }
            createPagingBoardComment(data.totalPages, data.number,boardId)

        })
        .catch(error => console.error('Error:', error));
}

function displayComments(data) {
    const list = document.getElementById('boardCommentList');
    const boardCommentId = document.getElementById('boardCommentId').getAttribute('id-value');
    list.innerHTML = '';  // Clear previous comments
    data.content.forEach(comment => {

        const formattedDate = moment(comment.createdDate).format('YYYY-MM-DD HH:mm');
        const commentCardElement = document.createElement('div');
        commentCardElement.className='card w-96 bg-base-100 shadow-xl';
        const commentHeaderElement = document.createElement('div');
        commentHeaderElement.className='card-body item-center text-left';
        const commentTitle = document.createElement('h2');
        commentTitle.className='card-title';
        commentTitle.textContent = `작성자: ${comment.memberDTO.name}, 작성일: ${formattedDate}`;
        const commentP = document.createElement('p');
        commentP.textContent = comment.comment;
        commentHeaderElement.appendChild(commentTitle);
        commentHeaderElement.appendChild(commentP);
        commentCardElement.appendChild(commentHeaderElement);
        if (boardCommentId === comment.memberDTO.name) {
            const flexDiv = document.createElement('div');
            flexDiv.className = 'flex flex-col justify-end';
            const deleteButton = document.createElement('button');
            deleteButton.className='btn btn-square btn-outline';
            deleteButton.value=comment.id;
            const updateButton = document.createElement('button');
            updateButton.textContent='수정';
            updateButton.value=comment.id;
            const updateSubmit =document.createElement('button');
            updateButton.addEventListener('click', ()=>{
                updateButton.style.display='none';
                updateSubmit.textContent='수정하기';
                updateSubmit.className='btn btn-square btn-outline';
                commentP.contentEditable = true;
                commentP.className='bg-slate-200';
                updateSubmit.addEventListener('click',()=>{
                    updateSubmit.style.display='none';
                    updateButton.style.display='block';
                    commentP.contentEditable = false;
                    commentP.className='';
                    const updateValueId = updateButton.value;
                    const commentChange= commentP.textContent;
                    updateComment(updateValueId,commentChange);
                })
            });
            deleteButton.innerHTML=`  <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
                    </svg>`;
            deleteButton.addEventListener('click', () => {
                const commentId = deleteButton.value;
                deleteComment(commentId);
            });
            flexDiv.appendChild(updateSubmit);
            flexDiv.appendChild(updateButton);
            flexDiv.appendChild(deleteButton);
            commentCardElement.appendChild(flexDiv);
        }
        list.appendChild(commentCardElement);
    });
}

function createPagingBoardComment(totalPages, currentPage, boardId) {
    const pagingButtonDiv = document.getElementById('pagingComment');
    pagingButtonDiv.innerHTML = '';

    for (let i = 0; i < totalPages; i++) {
        var pageButton = document.createElement('button');
        pageButton.className = "pagingComment";
        pageButton.textContent = i + 1;
        (function (page) {
            pageButton.onclick = function () {
                fetchComments(boardId, page, 10);
            };
        })(i);
        pagingButtonDiv.appendChild(pageButton);
    }
}

function insertComment() {
    const commentInput = document.getElementById('commentInput').value;
    if(commentInput.trim()< 1){
        alert("공백 댓글을 작성하실수 없습니다");
        return;
    }
    const data = {
        boardDTO: {
            id: boardId
        },
        comment: commentInput
    };

    fetch(`/board/comment/save`, {
        method: "POST",
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })
        .then(response => {
            if (response.redirected) {
                window.location.href = response.url;
            } else {
                return response.json();
            }
        })
        .then(data => {
            if (data) {
                alert(data);
            }
        })
        .catch(error => console.error('Error:', error));
}

function deleteComment(commentId) {
    const data = {
        boardDTO: {
            id: boardId
        },
        id : commentId,

    };

    fetch(`/board/comment/delete`, {
        method: "POST",
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })
        .then(response => {
            if (response.redirected) {
                window.location.href = response.url;
            } else {
                return response.json();
            }
        })
        .then(data => {
            if (data) {
                alert(data);
            }
        })
        .catch(error => console.error('Error:', error));
}
function updateComment(commentId, commentChange) {
    if(commentChange.trim()< 1){
        alert('공백 댓글을 수정하실 수 없습니다');
        return;
    }
    const data = {
        boardDTO: {
            id: boardId
        },
        id : commentId,
        comment: commentChange
    };

    fetch(`/board/comment/update`, {
        method: "POST",
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })
        .then(response => {
            if (response.redirected) {
                window.location.href = response.url;
            } else {
                return response.json();
            }
        })
        .then(data => {
            if (data) {
                alert(data);
            }
        })
        .catch(error => console.error('Error:', error));
}