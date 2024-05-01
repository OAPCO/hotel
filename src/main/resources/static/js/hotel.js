console.log("hotel.js 호출되었음");

let storeSelect = (function () {

    function selectDistOfStore(selectedDist){

        $.ajax({
            type: 'GET',
            url: '/selectstore', // 서버에서 storeList를 반환하는 엔드포인트
            data: { distName: selectedDist},
            contentType : "application/json; charset=utf-8",

            success: function(data) {


                var distbrandOfStore = data.distbrandOfStore;
                var distOfBrand = data.distOfBrand;

                $('#brandSelect').empty();
                $('#brandSelect').append($('<option>').val('').text('전체'));
                $('#storeSelect').empty();
                $('#storeSelect').append($('<option>').val('').text('전체'));

                $.each(distOfBrand, function(index, distOfBrand) {
                    console.log(distOfBrand)
                    $('#brandSelect').append($('<option>').val(distOfBrand.brandName).text(distOfBrand.brandName));
                });

                $.each(distbrandOfStore, function(index, distbrandOfStore) {
                    console.log(distbrandOfStore)
                    $('#storeSelect').append($('<option>').val(distbrandOfStore.storeName).text(distbrandOfStore.storeName));
                });



            },
            error: function(xhr, status, error) {
                console.error('Failed to retrieve stores: ' + error);
            }
        });

    };

    function selectDistAndBrandOfStore(selectedBrand) {

        $.ajax({
            type: 'GET',
            url: '/selectstore',
            data: { brandName: selectedBrand},
            contentType : "application/json; charset=utf-8",

            success: function(data) {

                var distbrandOfStore = data.distbrandOfStore;

                $('#storeSelect').empty();
                $('#storeSelect').append($('<option>').val('').text('전체'));


                $.each(distbrandOfStore, function(index, distbrandOfStore) {
                    console.log(distbrandOfStore)
                    $('#storeSelect').append($('<option>').val(distbrandOfStore.storeName).text(distbrandOfStore.storeName));
                });

            },
            error: function(xhr, status, error) {
                console.error('Failed to retrieve stores: ' + error);
            }
        });

    };




    return {
        selectDistOfStore  : selectDistOfStore,
        selectDistAndBrandOfStore : selectDistAndBrandOfStore
    };

})();