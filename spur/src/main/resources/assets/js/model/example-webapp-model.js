/**
 *    Sample Model
 *    All models go in the global 'EG' object
 *    Models are where business logic lives. Model validation, model service calls, error handling, etc. all go here and nowhere else
 */

(function () {
    "use strict";

    // Model
    EG.Hotel = Backbone.Model.extend({
        // Default attributes of this model
        defaults: {
            name: "The Jordan",
            description: "Fake hotel description."
        },

        // Validation called by model.save()
        validate: function(newAttributes) {
            if (isNaN(newAttributes.address.zipCode)) return "ZIP code must be a number";
        },

        // Use parse to fix ugly data or make it otherwise more compatible
        parse: function(data) {
            var info = data.propertyInfo;
            var hotel = {};

            hotel.id            = info.hotelID;
            hotel.name          = info.name;
            hotel.isPreferred   = data.isPreferred;
            hotel.availability  = data.availabilityStatus;
            hotel.stars         = info.starCount;
            hotel.reviews       = info.averageReviewScore.replace(".", "-");
            hotel.photos        = info.photos;
            hotel.address       = info.location.address.firstLine;
            hotel.coordinates   = [info.location.latitude, info.location.longitude];
            hotel.type          = "tooltip"; // for Maps

            return hotel;
        }
    });

    // Collection
    EG.HotelCollection = Backbone.Collection.extend({
        model: EG.Hotel,

        initialize: function(models) {
            // filtered starts out the same as the "source" Collection
            this.filtered = new Backbone.Collection(models);
            this.on('change update reset', this.updateFiltered);
        },

        url: "/spur/",

        // Method for filtering by Hotel name
        searchByName: function(query) {
            var matches;

            if (query === "") {
                return this.filtered;
            }
            else {
                matches = this.filtered.filter(function(hotel) {
                    if (hotel.get("name").toLowerCase().search(query.toLowerCase()) !== -1) {
                        return hotel;
                    }
                });
                return new EG.HotelCollection(matches); // returning new Collection allows for chaining, e.g. hotels.searchByName(query).filterBy(filterType)
            }
        },

        // Method for filtering by any of the filters at this.filters
        updateFiltered: function(filterType) {
            var filtered, val;
            var filters = this.filters;
            var activeFilters = this.activeFilters;

            // Activate/de-active this filter
            if (filterType) {
                filters[filterType].active = filters[filterType].active ? false : true;
            }

            activeFilters = _.filter(filters, function(filter) {
                if (filter.active) {
                    return filter;
                }
            });

            // There are active filters, so filter
            if (activeFilters.length > 0) {
                filtered = this.filter(function(hotel) {
                    // If this Hotel passes any active filter, then it's a match and we keep it
                    // TODO Why can't I return the Hotel from within each()?
                    var match = false;
                    _.each(activeFilters, function(filter) {
                        val = hotel.get(filter.attr);

                        if (_.isFunction(filter.test)) {
                            if (filter.test(val)) {
                                match = true;
                            }
                            else {
                                match = false;
                            }
                        }
                        else if (_.isBoolean(filter.test)) {
                            if (val) {
                                match = true;
                            }
                            else {
                                match = false;
                            }
                        }
                        else if (_.isString(filter.test)) {
                            if (filter.test === val) {
                                match = true;
                            }
                            else {
                                match = false;
                            }
                        }
                    });

                    if (match) {
                        return hotel;
                    }
                });
                this.filtered.reset(filtered);
                return this.filtered;
            }
            // There are no active filters, so just return the unfiltered Collection
            else {
                this.filtered.reset(this.models);
                return this;
            }

        },

        filtered: null,

        // Filters - attr is the Model attribute, test is what qualifies the attribute, active is for toggling state
        filters: {
            preferred: {
                attr: "isPreferred",
                test: true,
                active: false
            },
            fourStar: {
                attr: "stars",
                test: function(val) {
                    return val >= 4.0;
                },
                active: false
            },
            availability: {
                attr: "availability",
                test: "Available",
                active: false
            }
        },

        activeFilters: null

    });

})();
