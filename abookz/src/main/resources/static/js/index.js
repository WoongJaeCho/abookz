function searchBooks() {
    var query = document.getElementById('query').value;
    var resultsDiv = document.getElementById('searchResults');
    resultsDiv.innerHTML = '';
    if (!query.trim()) {
        alert('검색어를 입력해주세요');
        return;
    }
    // 우리사이트에서 DB에 저장된것 sort
    if (!query.trim()) {

        fetch('/search')
            .then(response => response.json())
            .then(data => {
                if (data && data.length > 0) {

                    var table = document.createElement('table');
                    table.className = 'query_table';
                    // 이미지 행
                    var coverRow = document.createElement('tr');
                    coverRow.className = 'query_tr';
                    var coverCell = document.createElement('td');
                    coverCell.className = 'query_td';
                    coverCell.setAttribute('rowspan', '5');
                    coverCell.setAttribute('width', '300');
                    var coverImage = document.createElement('img');
                    coverImage.src = book.cover;
                    coverImage.className="cover_img"
                    coverImage.alt = '책 이미지';
                    var coverDetail = document.createElement('a');
                    coverDetail.href = "/content/" + book.isbn13;
                    coverDetail.appendChild(coverImage);
                    coverCell.appendChild(coverDetail);
                    coverRow.appendChild(coverCell);
                    table.appendChild(coverRow);

                    // 제목
                    var titleRow = document.createElement('tr');
                    var titleCell = document.createElement('td');
                    titleCell.textContent = book.title;
                    titleRow.appendChild(titleCell);
                    table.appendChild(titleRow);

                    // 작가
                    var authorRow = document.createElement('tr');
                    authorRow.className="query_tr";
                    var authorCell = document.createElement('td');
                    authorCell.className="query_td";
                    authorCell.textContent = book.author;
                    authorRow.appendChild(authorCell);
                    table.appendChild(authorRow);

                    //출판날짜
                    var pubDateRow = document.createElement('tr');
                    pubDateRow.className="query_tr";
                    var pubDateCell = document.createElement('td');
                    pubDateCell.className="query_td";
                    pubDateCell.textContent = book.pubDate;
                    pubDateRow.appendChild(pubDateCell);
                    table.appendChild(pubDateRow);

                    resultsDiv.appendChild(table);
                    //화면이 모드 로드된 후에 기능이 실행된다 DOMContentLoaded
                    document.addEventListener('DOMContentLoaded', function() {
                        if(document.getElementById('book_container')) {
                            document.getElementById('book_container').style.display = 'none';
                       }
                        });
                } else {
                    resultsDiv.textContent = 'No books found';
                }
            })
            .catch(error => {
                console.error('Error:', error);
            });
    } else {
        // 검색어가 비어 있지 않다면 검색어를 포함하여 서버에 요청
        fetch('/search?query=' + query, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({query: query})
        })

            .then(response => response.json())
            .then(data => {
                if (data && data.length > 0) {
                    data.forEach(book => {

                        var table = document.createElement('table');
                        table.className = 'query_table';
                        // 이미지 행
                        var coverRow = document.createElement('tr');
                        coverRow.className = 'query_tr';
                        var coverCell = document.createElement('td');
                        coverCell.className = 'query_td';
                        coverCell.setAttribute('rowspan', '5');
                        coverCell.setAttribute('width', '300');
                        var coverImage = document.createElement('img');
                        coverImage.src = book.cover;
                        coverImage.className="cover_img"
                        coverImage.alt = '책 이미지';
                        var coverDetail = document.createElement('a');
                        coverDetail.href = "/content/" + book.isbn13;
                        coverDetail.appendChild(coverImage);
                        coverCell.appendChild(coverDetail);
                        coverRow.appendChild(coverCell);
                        table.appendChild(coverRow);

                        // 제목
                        var titleRow = document.createElement('tr');
                        var titleCell = document.createElement('td');
                        titleCell.textContent = book.title;
                        titleRow.appendChild(titleCell);
                        table.appendChild(titleRow);

                        // 작가
                        var authorRow = document.createElement('tr');
                        authorRow.className="query_tr";
                        var authorCell = document.createElement('td');
                        authorCell.className="query_td";
                        authorCell.textContent = book.author;
                        authorRow.appendChild(authorCell);
                        table.appendChild(authorRow);

                        //출판날짜
                        var pubDateRow = document.createElement('tr');
                        pubDateRow.className="query_tr";
                        var pubDateCell = document.createElement('td');
                        pubDateCell.className="query_td";
                        pubDateCell.textContent = book.pubDate;
                        pubDateRow.appendChild(pubDateCell);
                        table.appendChild(pubDateRow);

                        resultsDiv.appendChild(table);

                    });
                    //화면이 모드 로드된 후에 기능이 실행된다 DOMContentLoaded
                    document.addEventListener('DOMContentLoaded', function() {
                    if(document.getElementById('book_container')){
                        document.getElementById('book_container').style.display = 'none';
                    }
                    if(document.getElementById('shelf_container')){
                        document.getElementById('shelf_container').style.display = 'none';
                    }
                    });
                } else {
                    resultsDiv.textContent = 'No results found';
                }
            })
            .catch(error => {
                console.error('Error:', error);
            });
    }
}