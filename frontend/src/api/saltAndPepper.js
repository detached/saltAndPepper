import {saltAndPepperClient} from "../config/axiosConfig";
import {mockConfig} from "../config/mockConfig";
import {Author, Recipe, RecipeImage} from "../model/recipe";
import {getAccessToken} from "../service/tokenStore";
import {ArrayModel, Model} from "objectmodel"

export const FilterKey = {
    AUTHOR: "AUTHOR",
    CATEGORY: "CATEGORY",
    CUISINE: "CUISINE",
};

export const OrderField = {
    CREATED_AT: "CREATED_AT",
    TITLE: "TITLE",
};

export const SortDir = {
    ASC: "ASC",
    DESC: "DESC",
};

export class FilterValue extends Model({
    value: String,
    label: String
}) {
}

export class SearchFilter extends Model({
    AUTHOR: ArrayModel([FilterValue, String]),
    CATEGORY: ArrayModel([FilterValue, String]),
    CUISINE: ArrayModel([FilterValue, String])
}).defaultTo({
    AUTHOR: [],
    CATEGORY: [],
    CUISINE: []
}) {
}

export class Order extends Model({
    field: [OrderField.CREATED_AT, OrderField.TITLE],
    direction: [SortDir.DESC, SortDir.ASC]
}) {
}

export class Page extends Model({
    size: Number,
    number: Number,
    maxNumber: Number
}).defaultTo({
    maxNumber: 0
}) {
}

export class SearchRequest extends Model({
    searchQuery: String,
    page: Page,
    filter: SearchFilter,
    order: Order
}) {
}

export class SearchResponseData extends Model({
    id: String,
    imageUrl: String,
    title: String,
    category: String,
    cuisine: String,
    author: String
}) {
}

export class SearchResponse extends Model({
    data: ArrayModel(SearchResponseData),
    page: Page,
    possibleFilter: SearchFilter
}) {
}

export class NewRecipeRequest extends Model({
    title: String,
    category: String,
    cuisine: String,
    yields: String,
    ingredients: String,
    instructions: String,
    modifications: String
}) {
}

export class NewRecipeResponse extends Model({
    id: String
}) {
}

export class Profile extends Model({
    id: String,
    name: String,
    role: String,
    isAllowedToInvite: Boolean
}) {
}

export class ChangePasswordRequest extends Model({
    oldPassword: String,
    newPassword: String
}) {
}

export class InvitationCodeResponse extends Model({
    code: String
}) {
}

export class InvitationInfoResponse extends Model({
    invitingUser: String
}) {
}

export class CreateNewCommentRequest extends Model({}) {

}

export class CommentResponse extends Model({}) {

}

export class CommentsResponse extends Model({}) {

}

export class AuthResponse extends Model({
    accessToken: String,
    refreshToken: String
}) {
}

function doRequest(method, url, data, headers) {
    const token = getAccessToken();
    let headersAndAuth = {...headers};

    if (token) {
        headersAndAuth.Authorization = `Bearer ${token}`;
    }

    return saltAndPepperClient.request({
        method,
        url,
        data,
        headers: headersAndAuth,
    });
}

export const SaltAndPepper = {
    /**
     * @param searchRequest {SearchRequest}
     * @returns {Promise<SearchResponse>}
     */
    search: async function (searchRequest) {
        if (mockConfig.enabled) {
            console.log("search: " + JSON.stringify(searchRequest));
            return new SearchResponse({
                    data: [
                        new SearchResponseData({
                            id: searchRequest.page.number + "1",
                            imageUrl: "https://picsum.photos/150",
                            title: searchRequest.searchQuery + "1" + searchRequest.page.number,
                            category: "TestCategory1",
                            cuisine: "TestCuisine1",
                            author: "Author1"
                        }),
                        new SearchResponseData({
                            id: searchRequest.page.number + "2",
                            imageUrl: "https://picsum.photos/150",
                            title: searchRequest.searchQuery + "2" + searchRequest.page.number,
                            category: "TestCategory2",
                            cuisine: "TestCuisine2",
                            author: "Author2"
                        }),
                        new SearchResponseData({
                            id: searchRequest.page.number + "3",
                            imageUrl: "https://picsum.photos/150",
                            title: searchRequest.searchQuery + "3" + searchRequest.page.number,
                            category: "TestCategory3",
                            cuisine: "TestCuisine3",
                            author: "Author3"
                        }),
                        new SearchResponseData({
                            id: searchRequest.page.number + "4",
                            imageUrl: "https://picsum.photos/150",
                            title: searchRequest.searchQuery + "4" + searchRequest.page.number,
                            category: "TestCategory4",
                            cuisine: "TestCuisine4",
                            author: "Author4"
                        }),

                        new SearchResponseData({
                            id: searchRequest.page.number + "5",
                            imageUrl: "https://picsum.photos/150",
                            title: searchRequest.searchQuery + "5" + searchRequest.page.number,
                            category: "TestCategory5",
                            cuisine: "TestCuisine5",
                            author: "Author5"
                        }),
                        new SearchResponseData({
                            id: searchRequest.page.number + "6",
                            imageUrl: "https://picsum.photos/150",
                            title: searchRequest.searchQuery + "6" + searchRequest.page.number,
                            category: "TestCategory6",
                            cuisine: "TestCuisine6",
                            author: "Author7"
                        }),
                        new SearchResponseData({
                            id: searchRequest.page.number + "7",
                            imageUrl: "https://picsum.photos/150",
                            title: searchRequest.searchQuery + "7" + searchRequest.page.number,
                            category: "TestCategory7",
                            cuisine: "TestCuisine7",
                            author: "Author7"
                        }),
                        new SearchResponseData({
                            id: searchRequest.page.number + "8",
                            imageUrl: "https://picsum.photos/150",
                            title: searchRequest.searchQuery + "8" + searchRequest.page.number,
                            category: "TestCategory8",
                            cuisine: "TestCuisine8",
                            author: "Author8"
                        }),
                    ],
                    page: new Page({size: searchRequest.page.size, number: searchRequest.page.number, maxNumber: 10}),
                    possibleFilter: new SearchFilter({
                        AUTHOR: [
                            new FilterValue({value: "1", label: "Author1"}),
                            new FilterValue({value: "2", label: "Author2"}),
                            new FilterValue({value: "3", label: "Author3"}),
                            new FilterValue({value: "4", label: "Author4"}),
                            new FilterValue({value: "5", label: "Author5"}),
                            new FilterValue({value: "6", label: "Author6"}),
                            new FilterValue({value: "7", label: "Author7"}),
                            new FilterValue({value: "8", label: "Author8"}),
                            new FilterValue({value: "9", label: "Author9"}),
                            new FilterValue({value: "10", label: "Author10"}),
                            new FilterValue({value: "11", label: "Author11"})
                        ],
                        CATEGORY: [
                            new FilterValue({value: "TestCategory1", label: "TestCategory1"}),
                            new FilterValue({value: "TestCategory2", label: "TestCategory2"}),
                            new FilterValue({value: "TestCategory3", label: "TestCategory3"}),
                        ],
                        CUISINE: [
                            new FilterValue({value: "TestCuisine1", label: "TestCuisine1"}),
                            new FilterValue({value: "TestCuisine2", label: "TestCuisine2"}),
                        ]
                    })
                }
            );
        } else {
            const response = await doRequest("POST", "/recipe/search", searchRequest);
            return response.data;
        }
    },

    /**
     *
     * @param newRecipeRequest {NewRecipeRequest}
     * @returns {Promise<NewRecipeResponse>}
     */
    newRecipe: async function (newRecipeRequest) {
        if (mockConfig.enabled) {
            console.log("newRecipe: " + JSON.stringify(newRecipeRequest));
            return new NewRecipeResponse({id: "1"});
        } else {
            const response = await doRequest("POST", "/recipe/", newRecipeRequest);
            return response.data;
        }
    },

    /**
     * @returns {Promise<Profile>}
     */
    getProfile: async function () {
        if (mockConfig.enabled) {
            console.log("getProfile");
            return new Profile({id: "1", name: "Testname", role: "Testrole", isAllowedToInvite: true});
        } else {
            const response = await doRequest("GET", "/profile/");
            if (response.status !== 200) {
                throw new Error(response.statusText);
            }
            return response.data;
        }
    },

    /**
     * @param changePasswordRequest {ChangePasswordRequest}
     * @returns {Promise<>}
     */
    changePassword: async function (changePasswordRequest) {
        if (mockConfig.enabled) {
            console.log("changePassword: " + JSON.stringify(changePasswordRequest));
            if (changePasswordRequest.oldPassword !== changePasswordRequest.newPassword) {
                throw new Error("Passwords don't match");
            }
        } else {
            const response = await doRequest(
                "PATCH",
                "/profile/password",
                changePasswordRequest
            );
            if (response.status !== 202) {
                throw new Error(response.statusText);
            }
        }
    },

    /**
     * @returns {Promise<InvitationCodeResponse|null>}
     */
    getInvitationCode: async function () {
        if (mockConfig.enabled) {
            console.log("getInvitationCode");
            return null;
            //return new InvitationCodeResponse("1234");
        } else {
            const response = await doRequest("GET", "/invitation");
            if (response.status === 200) {
                const data = response.data;
                InvitationCodeResponse.assertType(data);
                return data;
            }
        }
    },

    /**
     * @returns {Promise<InvitationCodeResponse>}
     */
    createInvitationCode: async function () {
        if (mockConfig.enabled) {
            console.log("createInvitationCode");
            return new InvitationCodeResponse({code: "1234"});
        } else {
            const response = await doRequest("POST", "/invitation");
            return response.data;
        }
    },

    /**
     * @param code {String}
     * @returns {Promise<InvitationInfoResponse>}
     */
    getInvitationInfo: async function (code) {
        if (mockConfig.enabled) {
            console.log("getInvitationInfo code: " + code);
            return new InvitationInfoResponse({invitingUser: "InvitingUser"});
        } else {
            const response = await doRequest("GET", "/invitation/" + code);
            return response.data;
        }
    },

    /**
     * @param code {String}
     * @param username {String}
     * @param password {String}
     * @returns {Promise<>}
     */
    registerByInvitation: async function (code, username, password) {
        if (mockConfig.enabled) {
            console.log(
                "useInvitation code: " +
                code +
                ", username: " +
                username +
                ", password: " +
                password
            );
        } else {
            const result = await doRequest("PUT", "/invitation/" + code, {
                username: username,
                password: password,
            });
            if (result.status !== 200) {
                throw Error(result.statusText);
            }
        }
    },

    /**
     * @param fileFormat {String}
     * @param file {File}
     * @returns {Promise<>}
     */
    import: async function (fileFormat, file) {
        if (mockConfig.enabled) {
            console.log("import " + fileFormat + " " + file.name);
        } else {
            const formData = new FormData();
            formData.append("file", file);
            const result = await doRequest(
                "POST",
                "/import/" + fileFormat,
                formData,
                {
                    headers: {
                        "Content-Type": "multipart/form-data",
                    },
                }
            );
            if (result.status !== 204) {
                throw Error(result.statusText);
            }
        }
    },

    /**
     * @param recipeId {String}
     * @returns {Promise<Recipe>}
     */
    getRecipe: async function (recipeId) {
        if (mockConfig.enabled) {
            console.log("getRecipe: " + recipeId);
            return new Recipe({
                id: recipeId,
                author: new Author({id: "1", name: "Author"}),
                title: "Title",
                category: "Category",
                cuisine: "Cuisine",
                yields: "Yields",
                ingredients: "Ingredients\nline2\nline3",
                instructions: "Instructions\nline2\nline3",
                modifications: "Modifications\nline2\nline3",
                images: [
                    new RecipeImage({
                        id: "1",
                        url: "https://picsum.photos/800/600",
                        thumbnailUrl: "https://picsum.photos/150"
                    }),
                    new RecipeImage({
                        id: "2",
                        url: "https://picsum.photos/600/800",
                        thumbnailUrl: "https://picsum.photos/150"
                    }),
                ]
            });
        } else {
            const response = await doRequest("GET", "/recipe/" + recipeId);
            return response.data;
        }
    },

    /**
     * @param recipeId {String}
     * @param image {File}
     * @returns {Promise<RecipeImage>}
     */
    addImageToRecipe: async function (recipeId, image) {
        if (mockConfig.enabled) {
            console.log(
                "addImageToRecipe: recipeId:" + recipeId + " image: " + image.name
            );
            return new RecipeImage({
                id: "123",
                url: "https://picsum.photos/800/600",
                thumbnailUrl: "https://picsum.photos/150"
            });
        } else {
            const formData = new FormData();
            formData.append("file", image);
            const response = await doRequest(
                "POST",
                "/recipe/" + recipeId + "/images/",
                formData,
                {
                    headers: {
                        "Content-Type": "multipart/form-data",
                    },
                }
            );
            return response.data;
        }
    },

    /**
     * @param recipe {Recipe}
     * @returns {Promise<Recipe>}
     */
    updateRecipe: async function (recipe) {
        if (mockConfig.enabled) {
            console.log("updateRecipe: " + JSON.stringify(recipe));
            return recipe;
        } else {
            const response = await doRequest("PUT", "/recipe/" + recipe.id, recipe);
            return response.data;
        }
    },

    /**
     * @param recipeId {String}
     * @returns {Promise<>}
     */
    deleteRecipe: async function (recipeId) {
        if (mockConfig.enabled) {
            console.log("deleteRecipe: " + recipeId);
        } else {
            const result = await doRequest("DELETE", "/recipe/" + recipeId);
            if (result.status !== 200) {
                throw Error(result.statusText);
            }
        }
    },

    /**
     * @param username {String}
     * @param password {String}
     * @returns {Promise<AuthResponse>}
     */
    login: async function (username, password) {
        if (mockConfig.enabled) {
            console.log("login " + username + " password " + password);
            return new AuthResponse({
                accessToken: "1234",
                refreshToken: "1234"
            });
        } else {
            const response = await saltAndPepperClient.post("/login", {
                username,
                password,
            });
            return new AuthResponse({
                accessToken: response.data["access_token"],
                refreshToken: response.data["refresh_token"]
            });
        }
    },

    /**
     * @param refreshToken {String}
     * @returns {Promise<AuthResponse>}
     */
    refreshToken: async function (refreshToken) {
        if (mockConfig.enabled) {
            console.log("refreshToken " + refreshToken);
            return new AuthResponse({
                accessToken: "1234",
                refreshToken: "1234"
            });
        } else {
            const response = await doRequest("POST", "/token", {
                grant_type: "refresh_token",
                refresh_token: refreshToken,
            });

            if (response.status !== 200) {
                throw Error(response.statusText);
            }

            return new AuthResponse({
                accessToken: response.data["access_token"],
                refreshToken: response.data["refresh_token"]
            });
        }
    },
};
