// "index" View's Presenter
EG["spur"].Presenters.Index = function () {
    "use strict";

    //----- Initialize -----
    var AppRouter;
    var $delegate = $('body');
    var hotels = new EG.HotelCollection;
    hotels.reset(EG.bootstrapped.hotels, {parse: true, silent: true});
    uitk.map.data = {
        hotels: {
            markerData: hotels.toJSON()
        }
    };


    //----- UI events -----
    $delegate.on('input', '#hotel-search', searchHotels);
    $delegate.on('click', '.hotel-filter', filterHotels);
    uitk.subscribe('map.marker.click', showHotelDetails);


    //----- Model events ------
    hotels.on('update reset', updateMarkerData);
    hotels.filtered.on('reset', updateMarkerData);


    //----- Document Ready event -----
    $(document).ready(function () {
        uitk.map.loadApi();
    });


    //----- Router events -----
    AppRouter = Backbone.Router.extend({
        routes: {
            "details/:id": showHotelDetails
        }
    });
    new AppRouter();
    Backbone.history.start();


    //----- Private functions/callbacks -----
    function searchHotels() {
        updateMarkerData(hotels.searchByName(this.value));
    }

    function filterHotels() {
        var $this = $(this);
        var stateIcon = $this.hasClass('active') ? $this.data('inactive-icon') : $this.data('active-icon');
        var filterType = this.id.replace("hotel-filter-type-", "");

        $this.stripClass('icon-').addClass('icon-' + stateIcon).toggleClass('active');
        updateMarkerData(hotels.updateFiltered(filterType));
    }

    function showHotelDetails(topic, data) {
        var hotel = hotels.get(data.marker.id); // The other option would be to call fetch() on the Collection, which would make an AJAX request to the service, but in this case all data was bootstrapped so no need to go back again.
        var results = Handlebars.templates['partials/details-preview'](hotel.attributes);

        $('#hotel-details').html(results);
    }

    function updateMarkerData(collection) {
        uitk.map.data.hotels.module.updateMarkers(collection.toJSON(), {remove: true, zoomToFit: false});
    }

    // http://stackoverflow.com/a/2644364/1037948
    $.fn.stripClass = function (partialMatch, endOrBegin) {
        var x = new RegExp((!endOrBegin ? "\\b" : "\\S+") + partialMatch + "\\S*", 'g');

        this.attr('class', function (i, c) {
            if (!c) return;
            return c.replace(x, '');
        });
        return this;
    };

};