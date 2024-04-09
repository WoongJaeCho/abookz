var readChange = document.getElementsByClassName("shelf_myShelf_read");
var popups = document.getElementsByClassName("popup");
var radios = document.getElementsByName("stats");
console.log(popups);
console.log(readChange);
for (var i = 0; i < readChange.length; i++) {
    let element = readChange[i];
    let popup = popups[i];
    let radio = radios[i];
    console.log(radio)
    element.addEventListener("click", function (event) {
        var x = event.clientX; // 클릭한 위치의 x 좌표
        var y = event.clientY; // 클릭한 위치의 y 좌표
        // 클릭된 요소의 부모 요소나 조상 요소에 readChange 클래스가 있는지 확인
        if (popup.style.display === 'none') {
            popup.style.position = 'fixed';
            popup.style.top = y + 'px';
            popup.style.left = x + 'px';
            popup.style.display = 'block';
        }
        else if(radio.checked) {
                let checked = radio.value;
                console.log("radio.value"+checked )
                let value = popup.getAttribute('data-value');
                console.log("id  value"+value)
            popup.style.display = 'none';
                var jsonData = {
                    bookDTO: value,
                    tag: checked
                }
                fetch('/readingUpdate', {
                    method: "POST",
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(jsonData)
                })
                    .then(response => {
                        if (!response.ok) {
                            throw new Error('서버 응답이 실패했습니다.');
                        }
                        return response.json();
                    })
                    .then(data => {
                        // 서버에서 받은 응답 데이터 처리
                        console.log('서버에서 받은 데이터:', data);

                        // 여기서부터 data를 사용하여 필요한 작업 수행
                    })
                    .catch(error => {
                        // 오류 처리
                        console.error('오류 발생:', error);
                    });

            } else {
                popup.style.display = 'none';
            }

    });
}