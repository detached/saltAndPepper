import { ArrayModel, Model } from "objectmodel";

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
  label: String,
}) {}

export class SearchFilter extends Model({
  AUTHOR: ArrayModel([FilterValue, String]),
  CATEGORY: ArrayModel([FilterValue, String]),
  CUISINE: ArrayModel([FilterValue, String]),
}).defaultTo({
  AUTHOR: [],
  CATEGORY: [],
  CUISINE: [],
}) {}

export class Order extends Model({
  field: [OrderField.CREATED_AT, OrderField.TITLE],
  direction: [SortDir.DESC, SortDir.ASC],
}) {}

export class Page extends Model({
  size: Number,
  number: Number,
  maxNumber: Number,
}).defaultTo({
  maxNumber: 0,
}) {}

export class SearchRequest extends Model({
  searchQuery: String,
  page: Page,
  filter: SearchFilter,
  order: Order,
}) {}

export class SearchResponseData extends Model({
  id: String,
  imageUrl: String,
  title: String,
  category: String,
  cuisine: String,
  author: String,
}) {}

export class SearchResponse extends Model({
  data: ArrayModel(SearchResponseData),
  page: Page,
  possibleFilter: SearchFilter,
}) {}

export class NewRecipeRequest extends Model({
  title: String,
  category: String,
  cuisine: String,
  yields: String,
  ingredients: String,
  instructions: String,
  modifications: String,
}) {}

export class NewRecipeResponse extends Model({
  id: String,
}) {}

export class Profile extends Model({
  id: String,
  name: String,
  role: String,
  isAllowedToInvite: Boolean,
}) {}

export class ChangePasswordRequest extends Model({
  oldPassword: String,
  newPassword: String,
}) {}

export class InvitationCodeResponse extends Model({
  code: String,
}) {}

export class InvitationInfoResponse extends Model({
  invitingUser: String,
}) {}

export class CreateNewCommentRequest extends Model({
  comment: String,
}) {}

export class CommentAuthor extends Model({
  id: String,
  name: String,
}) {}

export class CommentResponse extends Model({
  id: String,
  author: CommentAuthor,
  comment: String,
  createdOn: Date,
  canDelete: Boolean,
}) {}

export class AuthResponse extends Model({
  accessToken: String,
  refreshToken: String,
}) {}
