import {saltAndPepperClient} from "../config/axiosConfig";
import assertProperty from "../utils/assertProperty";
import {mockConfig} from "../config/mockConfig";

export class SearchRequest {
    /**
     * @param searchQuery {String}
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
     * @param id {String}
     * @param imageUrl {String}
     * @param title {String}
     * @param category {String}
     * @param cuisine {String}
     * @param author {String}
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
     * @param size {Number}
     * @param number {Number}
     * @param maxNumber {Number}
     */
    constructor(size, number, maxNumber = 0) {
        this.size = size;
        this.number = number;
        this.maxNumber = maxNumber;
    }
}

export class NewRecipeRequest {

    /**
     * @param title {String}
     * @param category {String}
     * @param cuisine {String}
     * @param yields {String}
     * @param ingredients {String}
     * @param instructions {String}
     * @param modifications {String}
     */
    constructor(title, category, cuisine, yields, ingredients, instructions, modifications) {
        this.title = title;
        this.category = category;
        this.cuisine = cuisine;
        this.yields = yields;
        this.ingredients = ingredients;
        this.instructions = instructions;
        this.modifications = modifications;
    }
}

export class NewRecipeResponse {

    /**
     * @param id {String}
     */
    constructor(id) {
        this.id = id;
    }

    static assertType(object) {
        assertProperty(object, "id");
    }
}

export class Profile {

    /**
     * @param name {String}
     * @param role {String}
     * @param isAllowedToInvite {Boolean}
     */
    constructor(name, role, isAllowedToInvite) {
        this.name = name;
        this.role = role;
        this.isAllowedToInvite = isAllowedToInvite;
    }

    static assertType(object) {
        assertProperty(object, "name");
        assertProperty(object, "role");
        assertProperty(object, "isAllowedToInvite");
    }
}

export class ChangePasswordRequest {

    /**
     * @param oldPassword {String}
     * @param newPassword {String}
     */
    constructor(oldPassword, newPassword) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }
}

export class InvitationLinkResponse {

    /**
     * @param invitationLink {String}
     */
    constructor(invitationLink) {
        this.invitationLink = invitationLink;
    }

    static assertType(object) {
        assertProperty(object, "invitationLink");
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

    /**
     *
     * @param newRecipeRequest {NewRecipeRequest}
     * @returns {Promise<NewRecipeResponse>}
     */
    newRecipe: async function (newRecipeRequest) {
        if (mockConfig.enabled) {
            console.log("newRecipe: " + newRecipeRequest);
            return new NewRecipeResponse("1");
        } else {
            const response = await saltAndPepperClient.post(
                "/recipe/",
                newRecipeRequest
            );
            const data = response.data;
            NewRecipeResponse.assertType(data);
            return data;
        }
    },

    /**
     * @returns {Promise<Profile>}
     */
    getProfile: async function () {
      if (mockConfig.enabled) {
          return new Profile("Testname", "Testrole", true);
      } else {
          const response = await saltAndPepperClient.get("/profile/");
          if (response.status !== 200) {
              throw new Error(response.statusText)
          }
          const data = response.data;
          Profile.assertType(data);
          return data;
      }
    },

    /**
     * @param changePasswordRequest {ChangePasswordRequest}
     * @returns {Promise<>}
     */
    changePassword: async function (changePasswordRequest) {
        if (mockConfig.enabled) {
            if (changePasswordRequest.oldPassword !== changePasswordRequest.newPassword) {
                throw new Error("Passwords don't match");
            }
        } else {
            const response = await saltAndPepperClient.patch("/profile/password", changePasswordRequest);
            if (response.status !== 202) {
                throw new Error(response.statusText)
            }
        }
    },

    /**
     * @returns {Promise<InvitationLinkResponse|null>}
     */
    getInvitationLink: async function() {
        if (mockConfig.enabled) {
            return null;
            //return new InvitationLinkResponse("http://localhost:3000/invitation/landing?code=1234");
        } else {
            const response = await saltAndPepperClient.get("/invitation");
            if (response.status === 200) {
                const data = response.data;
                InvitationLinkResponse.assertType(data);
                return data;
            }
        }
    },

    /**
     * @returns {Promise<InvitationLinkResponse>}
     */
    createInvitationLink: async function() {
        if (mockConfig.enabled) {
            return new InvitationLinkResponse("http://localhost:3000/invitation/landing?code=1234");
        } else {
            const response = await saltAndPepperClient.post("/invitation");
            const data = response.data;
            InvitationLinkResponse.assertType(data);
            return data;
        }
    },

    /**
     * @param fileFormat {String}
     * @param file {File}
     * @returns {Promise<>}
     */
    import: async function(fileFormat, file) {
        if (mockConfig.enabled) {
            console.log("Upload " + fileFormat + " " + file.name);
        } else {
            const formData = new FormData();
            formData.append("file", file);
            const result = await saltAndPepperClient.post("/import/" + fileFormat, formData, {
                headers: {
                    "Content-Type": "multipart/form-data"
                }
            });
            if (result.status !== 202) {
                throw Error(result.statusText);
            }
        }
    }
};
