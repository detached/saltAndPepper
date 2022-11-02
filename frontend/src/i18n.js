import i18n from "i18next";
import { initReactI18next } from "react-i18next";

const resources = {
  de: {
    translation: {
      title: "Salz und Pfeffer",
      description: "Eine online Rezeptverwaltung",
      copyToClipboard: "Kopieren",
      "menu.search": "Suche",
      "menu.newRecipe": "Neues Rezept",
      "menu.import": "Import",
      "menu.logout": "Abmelden",
      "menu.profile": "Profil",
      "search.submit": "Suchen",
      "search.table.title": "Titel",
      "search.table.category": "Kategorie",
      "search.table.cuisine": "Küche",
      "search.table.author": "Von",
      "recipe.author": "Von",
      "recipe.category": "Kategorie",
      "recipe.cuisine": "Küche",
      "recipe.yields": "Ergibt",
      "recipe.instructions": "Anleitung",
      "recipe.modifications": "Notizen",
      "recipe.ingredients": "Zutaten",
      "recipe.item": "Zutat",
      "recipe.unit": "Einheit",
      "recipe.edit": "Bearbeiten",
      "recipe.delete": "Löschen",
      "newRecipe.title": "Neues Rezept",
      "newRecipe.name": "Titel",
      "newRecipe.submit": "Speichern",
      "newRecipe.dropzone": "Bilder hinzufügen",
      "deleteRecipe.title": "Rezept löschen",
      "deleteRecipe.text": "unwiederuflich löschen?",
      "deleteRecipe.confirm": "Ja, wirklich löschen!",
      "deleteRecipe.success": "Rezept wurde gelöscht.",
      "editRecipe.title": "Rezept bearbeiten",
      "ingredientsSelector.add": "Zutat hinzufügen",
      "imageGallery.toggleUpload": "Bild hochladen",
      "imageGallery.upload": "Hochladen",
      "login.username": "Name",
      "login.password": "Passwort",
      "login.submit": "Anmelden",
      "login.failed": "Anmeldung fehlgeschlagen",
      "import.submit": "Import",
      "import.gourmet": "Gourmet XML Import",
      "import.dropzone": "Ziehe eine XML-Datei auf diese Fläche oder klicke sie an",
      "import.dropzone.file": "Datei",
      "import.success": "Import erfolgreich!",
      "import.error": "Es ist ein Fehler aufgetreten",
      "pagination.first": "erste Seite",
      "pagination.last": "letzte Seite",
      "profile.title": "Profil",
      "profile.name": "Name",
      "profile.role": "Rolle",
      "profile.changePassword.title": "Passwort ändern",
      "profile.changePassword.oldPassword": "altes Passwort",
      "profile.changePassword.newPassword": "neues Passwort",
      "profile.changePassword.save": "speichern",
      "profile.changePassword.success": "Passwort geändert!",
      "profile.createInvitation": "Freunde einladen",
      "profile.invitation.title": "Einladung",
      "profile.invitation.text":
        "Lade Freunde ein, indem du einen einmal gültigen Link generierst. Dieser Link ist 24 Stunden gültig.",
      "inviteRegistration.title": "Willkommen!",
      "inviteRegistration.text":
        "hat dich eingeladen deine Rezepte zu teilen! Erstelle einen Account:",
      "inviteRegistration.name": "Name",
      "inviteRegistration.password": "Passwort",
      "inviteRegistration.register": "Registrieren!",
      "inviteRegistration.error.userAlreadyExists":
        "Ein Nutzer mit diesem Namen existiert bereits.",
      "inviteRegistration.error.usernameInvalid":
        "Der Nutzername kann nicht verwendet werden.",
      "userRegistered.title": "Hallo",
      "userRegistered.text":
        "Du kannst dich jetzt mit deinem Passwort anmelden",
      "userRegistered.link": "Zum login!",
      "password.rules":
        "Das Passwort muss mindestens fünf Zeichen lang sein.",
      "password.error.passwordTooWeek": "Das gewählte Passwort ist zu schwach.",
      "password.error.wrongPassword": "Falsches Passwort!",
    },
  },
};

i18n.use(initReactI18next).init({
  resources,
  lng: "de",
  interpolation: {
    escapeValue: false,
  },
});

export default i18n;
