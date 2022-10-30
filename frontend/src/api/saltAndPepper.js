import {saltAndPepperClient} from "../config/axiosConfig";
import assertProperty from "../utils/assertProperty";
import {mockConfig} from "../config/mockConfig";

export class SearchRequest {
    /**
     * @param searchQuery {string}
     * @param page {Page}
     */
    constructor(searchQuery, page) {
        this.searchQuery = searchQuery;
        this.page = page;
    }
}

export class SearchResponse {
    /**
     * @param data {Array<SearchResponseData>}
     * @param page {Page}
     */
    constructor(data, page) {
        this.data = data;
        this.page = page;
    }

    static assertType(object) {
        assertProperty(object, "data");
        assertProperty(object, "page");
    }
}

export class SearchResponseData {
    /**
     * @param id {string}
     * @param imageUrl {string}
     * @param title {string}
     * @param category {string}
     * @param cuisine {string}
     * @param author {string}
     */
    constructor(id, imageUrl, title, category, cuisine, author) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.title = title;
        this.category = category;
        this.cuisine = cuisine;
        this.author = author;
    }
}

export class Page {
    /**
     * @param size {number}
     * @param number {number}
     * @param maxNumber {number}
     */
    constructor(size, number, maxNumber = 0) {
        this.size = size;
        this.number = number;
        this.maxNumber = maxNumber;
    }
}

export const SaltAndPepper = {
    /**
     * @param searchRequest {SearchRequest}
     * @returns {Promise<SearchResponse>}
     */
    search: async function (searchRequest) {
        if (mockConfig.enabled) {
            return new SearchResponse(
                [
                    new SearchResponseData(searchRequest.page.number + "1", "https://picsum.photos/150", searchRequest.searchQuery + "1" + searchRequest.page.number, "TestCategory1", "TestCuisine1", "Author1"),
                    new SearchResponseData(searchRequest.page.number + "2", "https://picsum.photos/150", searchRequest.searchQuery + "2" + searchRequest.page.number, "TestCategory2", "TestCuisine2", "Author2"),
                    new SearchResponseData(searchRequest.page.number + "3", "https://picsum.photos/150", searchRequest.searchQuery + "2" + searchRequest.page.number, "TestCategory2", "TestCuisine2", "Author2"),
                    new SearchResponseData(searchRequest.page.number + "4", "https://picsum.photos/150", searchRequest.searchQuery + "2" + searchRequest.page.number, "TestCategory2", "TestCuisine2", "Author2"),
                    new SearchResponseData(searchRequest.page.number + "5", "https://picsum.photos/150", searchRequest.searchQuery + "2" + searchRequest.page.number, "TestCategory2", "TestCuisine2", "Author2"),
                    new SearchResponseData(searchRequest.page.number + "6", "https://picsum.photos/150", searchRequest.searchQuery + "2" + searchRequest.page.number, "TestCategory2", "TestCuisine2", "Author2"),
                    new SearchResponseData(searchRequest.page.number + "7", "https://picsum.photos/150", searchRequest.searchQuery + "2" + searchRequest.page.number, "TestCategory2", "TestCuisine2", "Author2"),
                    new SearchResponseData(searchRequest.page.number + "8", "https://picsum.photos/150", searchRequest.searchQuery + "2" + searchRequest.page.number, "TestCategory2", "TestCuisine2", "Author2")
                ],
                new Page(searchRequest.page.size, searchRequest.page.number, 10)
            );
        } else {
            const response = await saltAndPepperClient.post(
                "/recipe/search",
                searchRequest
            );
            const data = response.data;
            SearchResponse.assertType(data);
            return data;
        }
    },
};
