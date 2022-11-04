import "./imageGallery.css";
import { useState } from "react";

export default function ImageGallery({ images }) {
  const [showLightbox, setShowLightbox] = useState(false);
  const [lightboxImageUrl, setLightboxImageUrl] = useState("");

  function openLightbox(imageUrl) {
    setLightboxImageUrl(imageUrl);
    setShowLightbox(true);
  }

  function closeLightbox() {
    setShowLightbox(false);
  }

  function imageList() {
    return images.map((image) => {
      return (
        <div key={image.id} className="pure-u-1-2 pure-u-sm-1-3 pure-u-md-1-5">
          <img
            className="pure-img"
            alt=""
            src={image.thumbnailUrl}
            onClick={() => openLightbox(image.url)}
          />
        </div>
      );
    });
  }

  return (
    <>
      <div className="pure-g image-gallery">{imageList()}</div>
      {showLightbox ? (
        <div className="image-gallery-lightbox">
          <img
            src={lightboxImageUrl}
            alt="lightbox"
            onClick={() => closeLightbox()}
          />
        </div>
      ) : null}
    </>
  );
}
