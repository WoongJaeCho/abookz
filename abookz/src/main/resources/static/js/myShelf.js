var readChange = document.getElementsByClassName("shelf_myShelf_read");
var popups = document.getElementsByClassName("popup");

function onRadioChange(popup, selectedValue) {
    let value = popup.getAttribute('data-value');
    console.log(value);
    console.log(selectedValue);
    popup.style.display = 'none';

    let jsonData = {
            id : value,
            tag: selectedValue

    };
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
            console.log('서버에서 받은 데이터:', data);
        })
        .catch(error => {
            console.error('오류 발생:', error);
        });
}

for (var i = 0; i < readChange.length; i++) {
    let element = readChange[i];
    let popup = popups[i];

    element.addEventListener("click", function (event) {
        var x = event.clientX; // 클릭한 위치의 x 좌표
        var y = event.clientY; // 클릭한 위치의 y 좌표

        popup.style.position = 'fixed';
        popup.style.top = y + 'px';
        popup.style.left = x + 'px';
        popup.style.display = 'block';

        let radiosInPopup = popup.querySelectorAll('input[type="radio"][name="stats"]');
        radiosInPopup.forEach(function(radio) {
            radio.addEventListener('change', function() {
                if (this.checked) {
                    onRadioChange(popup, this.value);
                }
            });
        });
    });
}
