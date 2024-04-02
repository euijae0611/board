document.addEventListener('DOMContentLoaded', function() {
    var emailAuthBtn = document.getElementById('emailAuthBtn');
    var container = document.getElementById('emailFeedback');
    var loading = document.getElementById('loading');
    emailAuthBtn.addEventListener('click', function() {
        var userEmail = document.getElementById('userEmail').value;
        if (!userEmail) {
            // showMessage('이메일 주소를 입력해주세요.'); // 메시지를 직접 표시하는 함수 호출
            container.style.display = 'block';
            container.innerText = '이메일 주소를 입력해주세요.';
            container.style.color = 'red';
            return;
        } else{
            container.style.display = 'none';
        }
        loading.style.display = 'block';

        fetch('/users/check-email', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: 'userEmail=' + encodeURIComponent(userEmail),
        })
            .then(response => response.json())
            .then(data => {
                loading.style.display = 'none';
                if (data.exists) {
                    // showMessage('이미 가입된 이메일입니다.'); // 메시지를 직접 표시하는 함수 호출
                    container.innerText = '이미 가입된 이메일입니다.';
                    container.style.display = 'block'
                    container.style.color = 'red';
                } else {
                    // showMessage('인증 이메일이 전송되었습니다. 이메일을 확인해주세요.');
                    container.innerText = '인증 이메일이 전송되었습니다. 이메일을 확인해주세요.';
                    container.style.color = 'green';
                    showVerificationInput();
                    startTimer(); // 타이머 시작
                    document.getElementById('emailAuthBtn').style.display = 'none'; // 이메일 인증 버튼 숨기기
                    document.getElementById('userEmail').disabled = true; // 이메일 입력칸 비활성화
                }
            })
            .catch(error => {
                loading.style.display = 'none';
                console.error('에러가 발생했습니다 : ', error);
                // showMessage('이메일 인증 요청 중 오류가 발생했습니다.'); // 메시지를 직접 표시하는 함수 호출
                container.innerText = '이메일 인증 요청 중 오류가 발생했습니다.';
                container.style.display = 'block';
                container.style.color = 'red';
            });
    });
});

function showVerificationInput() {
    document.getElementById('verificationInput').style.display = 'block';
}

// function showMessage(message) {
//     alert(message);
// }

document.getElementById('verifyBtn').addEventListener('click', function() {
    verifyCode();
});

function verifyCode() {
    var inputCode = document.getElementById('verificationCode').value;
    var userEmail = document.getElementById('userEmail').value;
    var container = document.getElementById('emailFeedback');

    fetch('/verify-code', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            email: userEmail,
            code: inputCode
        })
    })
        .then(response => {
            if (!response.ok) {
                return response.json().then(data => Promise.reject(data.message));
            }
            return response.json();
        })
        .then(data => {
            // 인증 성공 시
            container.style.display = 'block';
            container.innerHTML = '<p class="text-success">인증이 완료되었습니다.</p>';
            document.getElementById('verificationInput').style.display = 'none'; // 인증번호 입력란 숨기기
            clearInterval(timerId); // 타이머 멈춤
            document.getElementById('timerContainer').style.display = 'none'; // 타이머 숨기기
        })
        .catch(errorMessage => {
            // 인증 실패 시
            console.error('Error:', errorMessage);
            alert('정확한 인증번호를 입력해주세요.');
        });
}

function startTimer() {
    var time = 180; // 3분
    document.getElementById('timerContainer').style.display = 'block'; // 타이머 표시
    timerId = setInterval(function() {
        if (time <= 0) {
            clearInterval(timerId);
            document.getElementById('verificationCode').disabled = true;
            alert('인증 시간이 만료되었습니다.');
            document.getElementById('timerContainer').style.display = 'none'; // 타이머 숨기기
        } else {
            var minutes = Math.floor(time / 60);
            var seconds = time % 60;
            document.getElementById('timer').textContent = minutes + ':' + (seconds < 10 ? '0' : '') + seconds;
            time--;
        }
    }, 1000);
}
