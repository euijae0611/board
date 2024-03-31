document.addEventListener('DOMContentLoaded', function() {
    var emailAuthBtn = document.getElementById('emailAuthBtn');
    emailAuthBtn.addEventListener('click', function() {
        var userEmail = document.getElementById('userEmail').value;
        if (!userEmail) {
            alert('이메일 주소를 입력해주세요.');
            return;
        }
        fetch('/users/check-email', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            body: 'userEmail=' + encodeURIComponent(userEmail)
        })
            .then(response => response.json())
            .then(data => {
                if (data.exists) {
                    alert('이미 가입된 이메일입니다.');
                } else {
                    alert('인증 이메일이 전송되었습니다. 이메일을 확인해주세요.');
                    showVerificationInput(); // 인증 번호 입력란과 확인 버튼을 표시
                    startTimer(); // 타이머 시작
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('이메일 인증 요청 중 오류가 발생했습니다.');
            });
    });
});

function showVerificationInput() {
    document.getElementById('verificationInput').style.display = 'block'; // 인증 번호 입력란과 확인 버튼을 표시
}

document.getElementById('verifyBtn').addEventListener('click', function() {
    verifyCode();
});

function verifyCode() {
    var inputCode = document.getElementById('verificationCode').value;
    var userEmail = document.getElementById('userEmail').value;

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
            var container = document.getElementById('emailFeedback');
            container.innerHTML = '<p class="text-success">인증이 완료되었습니다.</p>';
            document.getElementById('verificationInput').style.display = 'none'; // 인증번호 입력란 숨기기
            document.getElementById('emailAuthBtn').style.display = 'none'; // 이메일 인증 버튼 숨기기
            document.getElementById('userEmail').disabled = true; // 이메일 입력칸 비활성화
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
