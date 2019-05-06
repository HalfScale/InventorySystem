var transactionBttn = document.querySelector('.pos-transaction-bttn');
var transactionModal = document.getElementById('pos-transaction-modal');
var transactionModalClose = document.querySelector('.transaction-modal-close');
var transactionModalTbody = document.querySelector('#pos-transaction-modal tbody');

transactionBttn.onclick = function() {
    transactionModal.style.display = 'block';
    
    var xmlhttp = new XMLHttpRequest();
    var url = '/InvenotrySystem/SystemController';
    var param = '?command=LIST_TRANSACTION';
}

transactionModalClose.onclick = function() {
    transactionModal.style.display = 'none';
}