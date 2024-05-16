console.log("hotel.js 호출되었음");

let hotel = (function () {

    
    //distchief/store/list의 총판 셀렉트박스 선택시
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



    //distchief/store/list의 브랜드 셀렉트박스 선택시
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




    function registerDistOfStore(selectedDist){

        $.ajax({
            type: 'GET',
            url: '/registerstore', // 서버에서 storeList를 반환하는 엔드포인트
            data: { distName: selectedDist},
            contentType : "application/json; charset=utf-8",

            success: function(data) {

                var distOfManager = data.distOfManager;
                var distOfBrand = data.distOfBrand;

                $('#brandSelect').empty();
                $('#brandSelect').append($('<option>').val('').text('전체'));
                $('#managerSelect').empty();
                $('#managerSelect').append($('<option>').val('').text('전체'));

                $.each(distOfBrand, function(index, distOfBrand) {
                    console.log(distOfBrand)
                    $('#brandSelect').append($('<option>').val(distOfBrand.brandName).text(distOfBrand.brandName));
                });

                $.each(distOfManager, function(index, distOfManager) {
                    console.log(distOfManager)
                    $('#managerSelect').append($('<option>').val(distOfManager.managerName).text(distOfManager.managerName));
                });

            },
            error: function(xhr, status, error) {
                console.error('Failed to retrieve stores: ' + error);
            }
        });

    };


    function searchRoomImage(roomIdx){

        $.ajax({
            type: 'GET',
            url: '/roomimage/'+roomIdx,
            contentType : "application/json; charset=utf-8",

            success: function(data) {

                // 데이터를 HTML 요소에 추가
                var imageContainer = $('#image-container');
                imageContainer.empty(); // 기존 내용을 비움

                // data가 배열이므로 각 항목을 처리
                data.forEach(function(imageDTO) {
                    var imgElement = $('<img>').attr('src', imageDTO.url);
                    imageContainer.append(imgElement);
                });


            },
            error: function(xhr, status, error) {
                console.error('Failed to retrieve stores: ' + error);
            }
        });

    };






    //값 반환
    return {
        selectDistOfStore  : selectDistOfStore,
        selectDistAndBrandOfStore : selectDistAndBrandOfStore,
        registerDistOfStore : registerDistOfStore
    };

})();