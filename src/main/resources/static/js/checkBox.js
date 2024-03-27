document.getElementById('agreeAll').addEventListener('change', function() {
    // '모든 항목에 동의합니다' 체크박스를 제외한 모든 체크박스를 선택합니다.
    var allCheckboxes = document.querySelectorAll('.form-check-input:not(#agreeAll)');
    // 성별 체크박스를 제외한 체크박스에 대해 동작합니다.
    allCheckboxes.forEach(function(checkbox) {
        if (checkbox !== document.getElementById('male') && checkbox !== document.getElementById('female')) {
            checkbox.checked = this.checked;
        }
    }.bind(this));
});

document.getElementById('agreeAll').addEventListener('change', function() {
    var allCheckboxes = document.querySelectorAll('#terms1, #terms2, #terms3, #terms4, #terms5, #terms6');
    allCheckboxes.forEach(function(checkbox) {
        checkbox.checked = this.checked;
    }, this);
});

document.getElementById('registrationForm').addEventListener('submit', function(event) {
    var requiredCheckboxes = document.querySelectorAll('#terms1, #terms2, #terms3, #terms4, #terms5, #terms6');
    var allChecked = Array.from(requiredCheckboxes).every(checkbox => checkbox.checked);
    if (!allChecked) {
        alert('필수 항목에 모두 동의해주세요');
        event.preventDefault();
    }
});