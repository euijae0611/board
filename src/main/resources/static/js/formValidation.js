document.addEventListener('DOMContentLoaded', function() {
    var form = document.getElementById('registrationForm');
    var password = document.getElementById('userPW');
    var confirmPassword = document.getElementById('upwConfirm');
    var phoneMiddle = document.getElementById('phoneMiddle');
    var phoneLast = document.getElementById('phoneLast');

    var passwordMismatchMessage = document.createElement('div');
    passwordMismatchMessage.style.color = 'red';
    passwordMismatchMessage.textContent = '비밀번호가 서로 일치하지 않습니다.';
    confirmPassword.parentNode.insertBefore(passwordMismatchMessage, confirmPassword.nextSibling);
    passwordMismatchMessage.style.display = 'none';

    var phoneMiddleError = document.createElement('div');
    phoneMiddleError.style.color = 'red';
    phoneMiddle.parentNode.insertBefore(phoneMiddleError, phoneMiddle.nextSibling);
    phoneMiddleError.style.display = 'none';

    var phoneLastError = document.createElement('div');
    phoneLastError.style.color = 'red';
    phoneLast.parentNode.insertBefore(phoneLastError, phoneLast.nextSibling);
    phoneLastError.style.display = 'none';

    function validatePassword() {
        if (password.value !== confirmPassword.value) {
            passwordMismatchMessage.style.display = 'block';
        } else {
            if(password.value && confirmPassword.value.length >= 1) {
                passwordMismatchMessage.style.display = 'block';
                passwordMismatchMessage.style.color = 'green';
                passwordMismatchMessage.textContent = '비밀번호가 서로 일치합니다.';
            } else {
                passwordMismatchMessage.style.display = 'none';
            }
        }
    }

    function validatePhonePart(inputElement, errorElement) {
        if (inputElement.value.length > 4) {
            inputElement.value = inputElement.value.slice(0, 4); // 최대 4자리까지 자르기
            errorElement.style.display = 'block';
        } else if (inputElement.value.length < 4) {
            errorElement.style.display = 'none';
        }
        // 숫자만 입력되도록 검증
        inputElement.value = inputElement.value.replace(/[^0-9]/g, '');
    }

    phoneMiddle.addEventListener('input', function() {
        validatePhonePart(phoneMiddle, phoneMiddleError);
    });
    phoneLast.addEventListener('input', function() {
        validatePhonePart(phoneLast, phoneLastError);
    });

    // 이벤트 리스너 연결
    confirmPassword.onkeyup = validatePassword;
    password.onkeyup = validatePassword;
    // phoneMiddle.oninput = validatePhonePart;
    // phoneLast.oninput = validatePhonePart;

    form.addEventListener('submit', function(event) {
        if (phoneMiddle.value.length > 4 || phoneLast.value.length > 4) {
            event.preventDefault();
            alert('전화번호에 오류가 발생했습니다.');
            return false;
        }
        if(password.value !== confirmPassword.value) {
            e.preventDefault();
            alert('비밀번호가 서로 일치하지 않습니다.')
        }
    });

    // form.addEventListener('submit',function(e){
    //     if(password.value !== confirmPassword.value) {
    //         e.preventDefault();
    //         alert('비밀번호가 서로 일치하지 않습니다.')
    //     }
    // })
});
