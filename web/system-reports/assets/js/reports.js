var xhr = new XMLHttpRequest();
var url = '/InventorySystem/FileController';
xhr.open('GET', url, true);
xhr.responseType = 'arraybuffer';
xhr.onreadystatechange = function () {
    if (this.readyState === XMLHttpRequest.DONE && this.status === 200) {
        console.log('success', this.response);
        var file = new Blob([this.response], {type: 'application/pdf'})
        var fileURL = URL.createObjectURL(file);
        window.open(fileURL, '_blank', 'menubar=yes,location=yes,resizable=yes,scrollbars=yes,width=1000,centerscreen,height=800,status=yes');
    }
};

xhr.send();