import { saltAndPepperClient } from "../config/axiosConfig";
import assertProperty from "../utils/assertProperty";
import { mockConfig } from "../config/mockConfig";
import { Author, Recipe, RecipeImage } from "../model/recipe";
import { getToken } from "../service/tokenStore";

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
  constructor(
    title,
    category,
    cuisine,
    yields,
    ingredients,
    instructions,
    modifications
  ) {
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
   * @param id {String}
   * @param name {String}
   * @param role {String}
   * @param isAllowedToInvite {Boolean}
   */
  constructor(id, name, role, isAllowedToInvite) {
    this.id = id;
    this.name = name;
    this.role = role;
    this.isAllowedToInvite = isAllowedToInvite;
  }

  static assertType(object) {
    assertProperty(object, "id");
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

export class InvitationCodeResponse {
  /**
   * @param code {String}
   */
  constructor(code) {
    this.code = code;
  }

  static assertType(object) {
    assertProperty(object, "code");
  }
}

function doRequest(method, url, data, headers) {
  const token = getToken();
  let headersAndAuth = { ...headers };

  if (token) {
    headersAndAuth.Authorization = `Bearer ${getToken()}`;
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
      return new SearchResponse(
        [
          new SearchResponseData(
            searchRequest.page.number + "1",
            "https://picsum.photos/150",
            searchRequest.searchQuery + "1" + searchRequest.page.number,
            "TestCategory1",
            "TestCuisine1",
            "Author1"
          ),
          new SearchResponseData(
            searchRequest.page.number + "2",
            "https://picsum.photos/150",
            searchRequest.searchQuery + "2" + searchRequest.page.number,
            "TestCategory2",
            "TestCuisine2",
            "Author2"
          ),
          new SearchResponseData(
            searchRequest.page.number + "3",
            "https://picsum.photos/150",
            searchRequest.searchQuery + "2" + searchRequest.page.number,
            "TestCategory2",
            "TestCuisine2",
            "Author2"
          ),
          new SearchResponseData(
            searchRequest.page.number + "4",
            "https://picsum.photos/150",
            searchRequest.searchQuery + "2" + searchRequest.page.number,
            "TestCategory2",
            "TestCuisine2",
            "Author2"
          ),
          new SearchResponseData(
            searchRequest.page.number + "5",
            "https://picsum.photos/150",
            searchRequest.searchQuery + "2" + searchRequest.page.number,
            "TestCategory2",
            "TestCuisine2",
            "Author2"
          ),
          new SearchResponseData(
            searchRequest.page.number + "6",
            "https://picsum.photos/150",
            searchRequest.searchQuery + "2" + searchRequest.page.number,
            "TestCategory2",
            "TestCuisine2",
            "Author2"
          ),
          new SearchResponseData(
            searchRequest.page.number + "7",
            "https://picsum.photos/150",
            searchRequest.searchQuery + "2" + searchRequest.page.number,
            "TestCategory2",
            "TestCuisine2",
            "Author2"
          ),
          new SearchResponseData(
            searchRequest.page.number + "8",
            "https://picsum.photos/150",
            searchRequest.searchQuery + "2" + searchRequest.page.number,
            "TestCategory2",
            "TestCuisine2",
            "Author2"
          ),
        ],
        new Page(searchRequest.page.size, searchRequest.page.number, 10)
      );
    } else {
      const response = await doRequest("POST", "/recipe/search", searchRequest);
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
      console.log("newRecipe: " + JSON.stringify(newRecipeRequest));
      return new NewRecipeResponse("1");
    } else {
      const response = await doRequest("POST", "/recipe/", newRecipeRequest);
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
      console.log("getProfile");
      return new Profile("1", "Testname", "Testrole", true);
    } else {
      const response = await doRequest("GET", "/profile/");
      if (response.status !== 200) {
        throw new Error(response.statusText);
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
      console.log("changePassword: " + JSON.stringify(changePasswordRequest));
      if (
        changePasswordRequest.oldPassword !== changePasswordRequest.newPassword
      ) {
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
      return new InvitationCodeResponse("1234");
    } else {
      const response = await doRequest("POST", "/invitation");
      const data = response.data;
      InvitationCodeResponse.assertType(data);
      return data;
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
      if (result.status !== 202) {
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
      let r = new Recipe(
        "Title",
        "Category",
        "Cuisine",
        "Yields",
        "Ingredients\nline2\nline3",
        "Instructions\nline2\nline3",
        "Modifications\nline2\nline3"
      );
      r.id = recipeId;
      r.author = new Author("1", "Author");
      r.images = [
        new RecipeImage(
          "1",
          "https://picsum.photos/800/600",
          "https://picsum.photos/150"
        ),
        new RecipeImage(
          "2",
          "https://picsum.photos/600/800",
          "https://picsum.photos/150"
        ),
      ];
      return r;
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
      return new RecipeImage(
        "123",
        "https://picsum.photos/800/600",
        "https://picsum.photos/150"
      );
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

  updateRecipe: async function (recipe) {
    if (mockConfig.enabled) {
      console.log("updateRecipe: " + JSON.stringify(recipe));
      return recipe;
    } else {
      const response = await doRequest("PUT", "/recipe/" + recipe.id, recipe);
      return response.data;
    }
  },

  login: async function (username, password) {
    if (mockConfig.enabled) {
      console.log("login " + username + " password " + password);
      return "1234";
    } else {
      const response = await saltAndPepperClient.post("/login", {
        username,
        password,
      });
      return response.data["access_token"];
    }
  },
};
